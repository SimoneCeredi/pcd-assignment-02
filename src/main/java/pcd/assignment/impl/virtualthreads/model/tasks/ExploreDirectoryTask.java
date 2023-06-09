package pcd.assignment.impl.virtualthreads.model.tasks;

import pcd.assignment.base.model.data.results.FileInfo;
import pcd.assignment.base.model.data.results.Result;
import pcd.assignment.base.model.data.results.ResultImpl;
import pcd.assignment.base.analyzer.SourceAnalyzerData;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Explore directory task as Runnable
 */
public class ExploreDirectoryTask implements Runnable {
    private final File directory;
    private final CompletableFuture<Result> currentDirFuture;
    private final SourceAnalyzerData data;


    public ExploreDirectoryTask(File directory,
                                CompletableFuture<Result> currentDirFuture,
                                SourceAnalyzerData data) {
        this.directory = directory;
        this.currentDirFuture = currentDirFuture;
        this.data = data;
    }

    /**
     * Computes the result of the current directory waiting the completion of its children.
     */
    @Override
    public void run() {
        List<Future<Result>> directoryFutures = new LinkedList<>();
        List<Future<FileInfo>> filesFutures = new LinkedList<>();
        exploreDirectory(directoryFutures, filesFutures);
        collectDirectoryData(directoryFutures);
        collectFilesData(filesFutures);
        if (!this.data.getResultsData().isStopped()) {
            try {
                this.data
                        .getResultsData()
                        .getResults()
                        .put(new ResultImpl(this.data.getCurrentIntervals().getCopy(),
                                this.data.getCurrentLongestFiles().getCopy()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        currentDirFuture.complete(new ResultImpl(this.data.getCurrentIntervals(),
                this.data.getCurrentLongestFiles()));
    }

    /**
     * Collect FileInfo(s) from ReadLinesTasks updating the data
     * @param filesFutures to get upon
     */
    private void collectFilesData(List<Future<FileInfo>> filesFutures) {
        for (var future : filesFutures) {
            try {
                FileInfo fileInfo = future.get();
                this.data.getCurrentIntervals().store(fileInfo);
                this.data.getCurrentLongestFiles().put(fileInfo);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Collect and ignore Result(s) from ExploreDirectoryTasks children
     * @param directoryFutures to get upon.
     */
    private void collectDirectoryData(List<Future<Result>> directoryFutures) {
        for (var future : directoryFutures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException ignored) {}
        }
    }

    /**
     * List the content of this.directory:
     *  - Start a Virtual thread (ExploreDirectoryTask) for each directory
     *  - Start a Virtual thread (ReadLinesTask) for each file
     * @param directoryFutures of ExploreDirectoryTasks children
     * @param filesFutures of ReadLinesTasks children
     */
    private void exploreDirectory(List<Future<Result>> directoryFutures,
                                  List<Future<FileInfo>> filesFutures) {
        if (this.directory.isDirectory()) {
            File[] files = this.directory.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length && !this.data.getResultsData().isStopped(); i++) {
                    if (files[i].isDirectory()) {
                        CompletableFuture<Result> subdirFuture = new CompletableFuture<>();
                        // Start a new virtual thread with a new future
                        Thread.ofVirtual().start(this.getExploreDirectoryTask(files[i], subdirFuture));
                        // Wait all subdirectories' tasks to complete to get their results
                        directoryFutures.add(subdirFuture);
                    } else {
                        if (files[i].getName().endsWith(".java")) {
                            CompletableFuture<FileInfo> subtaskFuture = new CompletableFuture<>();
                            ReadLinesTask task = new ReadLinesTask(files[i], subtaskFuture);
                            Thread.ofVirtual().start(task);
                            filesFutures.add(subtaskFuture);
                        }
                    }
                }
            }
        }
    }

    /**
     * Get a new ExploreDirectory task based on the current data.
     * @param file where to start
     * @param subdirFuture child future
     * @return new ExploreDirectoryTask
     */
    private ExploreDirectoryTask getExploreDirectoryTask(File file,
                                                         CompletableFuture<Result> subdirFuture){
        return new ExploreDirectoryTask(file, subdirFuture, this.data);
    }

}
