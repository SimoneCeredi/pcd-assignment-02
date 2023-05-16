package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.common.model.data.*;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.Pair;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    private void collectDirectoryData(List<Future<Result>> directoryFutures) {
        for (var future : directoryFutures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

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

    private ExploreDirectoryTask getExploreDirectoryTask(File file,
                                                         CompletableFuture<Result> subdirFuture) {
        return new ExploreDirectoryTask(file, subdirFuture, this.data);
    }
}
