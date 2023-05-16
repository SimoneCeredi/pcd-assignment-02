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
    private final ResultsData resultsData;
    private final Intervals intervals;
    private final LongestFiles longestFiles;

    public ExploreDirectoryTask(File directory,
                                CompletableFuture<Result> currentDirFuture,
                                ResultsData resultsData,
                                Intervals intervals,
                                LongestFiles longestFiles) {
        this.directory = directory;
        this.currentDirFuture = currentDirFuture;
        this.resultsData = resultsData;
        this.intervals = intervals;
        this.longestFiles = longestFiles;
    }

    @Override
    public void run() {
        List<Future<Result>> directoryFutures = new LinkedList<>();
        List<Future<FileInfo>> filesFutures = new LinkedList<>();
        exploreDirectory(directoryFutures, filesFutures);
        collectDirectoryData(directoryFutures);
        collectFilesData(filesFutures);
        if (!this.resultsData.isStopped()) {
            try {
                this.resultsData.getResults().put(
                        new ResultImpl(this.intervals.getCopy(), this.longestFiles.getCopy()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        currentDirFuture.complete(new ResultImpl(this.intervals, this.longestFiles));
    }

    private void collectFilesData(List<Future<FileInfo>> filesFutures) {
        for (var future : filesFutures) {
            try {
                FileInfo fileInfo = future.get();
                this.intervals.store(fileInfo);
                this.longestFiles.put(fileInfo);
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
                for (File file : files) {
                    if (!this.resultsData.isStopped()) {
                        if (file.isDirectory()) {
                            CompletableFuture<Result> subdirFuture = new CompletableFuture<>();
                            // Start a new virtual thread with a new future
                            Thread.ofVirtual().start(this.getExploreDirectoryTask(file, subdirFuture));
                            // Wait all subdirectories' tasks to complete to get their results
                            directoryFutures.add(subdirFuture);
                        } else {
                            if (file.getName().endsWith(".java")) {
                                CompletableFuture<FileInfo> subtaskFuture = new CompletableFuture<>();
                                ReadLinesTask task = new ReadLinesTask(file, subtaskFuture);
                                Thread.ofVirtual().start(task);
                                filesFutures.add(subtaskFuture);
                            }
                        }
                    }
                }
            }
        }
    }

    private ExploreDirectoryTask getExploreDirectoryTask(File file,
                                                         CompletableFuture<Result> subdirFuture) {
        return new ExploreDirectoryTask(
                file,
                subdirFuture,
                this.resultsData,
                this.intervals,
                this.longestFiles
        );
    }
}
