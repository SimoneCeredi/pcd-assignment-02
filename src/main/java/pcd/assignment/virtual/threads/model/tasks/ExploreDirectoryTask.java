package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.source.analyzer.StoppableSourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.tasks.strategy.MemorizeStrategy;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExploreDirectoryTask implements Runnable {
    private final File directory;
    private final CompletableFuture<Pair<Intervals, LongestFiles>> future;
    private final MemorizeStrategy strategy;
    private final BlockingQueue<Thread> threads;
    private final LongestFiles longestFiles;
    private final Intervals intervals;
    private final StoppableSourceAnalyzer sourceAnalyzer;

    public ExploreDirectoryTask(File directory, CompletableFuture<Pair<Intervals, LongestFiles>> future, Intervals intervals, LongestFiles longestFiles, MemorizeStrategy strategy, BlockingQueue<Thread> threads, StoppableSourceAnalyzer sourceAnalyzer) {
        this.directory = directory;
        this.future = future;
        this.intervals = intervals;
        this.longestFiles = longestFiles;
        this.strategy = strategy;
        this.threads = threads;
        this.sourceAnalyzer = sourceAnalyzer;
    }

    @Override
    public void run() {
        List<Future<Pair<Intervals, LongestFiles>>> directoryFutures = new LinkedList<>();
        List<Future<FileInfo>> filesFutures = new LinkedList<>();
        try {
            exploreDirectory(directoryFutures, filesFutures);
        } catch (InterruptedException ignored) {
        }
        collectDirectoryData(directoryFutures);
        collectFilesData(filesFutures);
        if (!this.sourceAnalyzer.shouldStop()) {
            this.strategy.saveResult(this.intervals, this.longestFiles);
        }
        future.complete(new Pair<>(this.intervals, this.longestFiles));

    }

    private void collectFilesData(List<Future<FileInfo>> filesFutures) {
        for (var future : filesFutures) {
            try {
                FileInfo fileInfo = future.get();
                this.intervals.store(fileInfo);
                this.longestFiles.put(fileInfo);
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
    }

    private void collectDirectoryData(List<Future<Pair<Intervals, LongestFiles>>> directoryFutures) {
        for (var future : directoryFutures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
    }

    private void exploreDirectory(List<Future<Pair<Intervals, LongestFiles>>> directoryFutures, List<Future<FileInfo>> filesFutures) throws InterruptedException {
        if (this.directory.isDirectory()) {
            File[] files = this.directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (this.sourceAnalyzer.shouldStop()) {
                        return;
                    }
                    if (file.isDirectory()) {
                        CompletableFuture<Pair<Intervals, LongestFiles>> future = new CompletableFuture<>();
                        this.threads.put(Thread.ofVirtual().start(this.getExploreDirectoryTask(file, future)));
                        directoryFutures.add(future);
                    } else {
                        if (file.getName().endsWith(".java")) {
                            CompletableFuture<FileInfo> future = new CompletableFuture<>();
                            ReadLinesTask task = new ReadLinesTask(file, future, sourceAnalyzer);
                            this.threads.put(Thread.ofVirtual().start(task));
                            filesFutures.add(future);
                        }
                    }
                }
            }
        }
    }

    private ExploreDirectoryTask getExploreDirectoryTask(File file, CompletableFuture<Pair<Intervals, LongestFiles>> future) {
        return new ExploreDirectoryTask(file, future, this.intervals, this.longestFiles, this.strategy, threads, this.sourceAnalyzer);
    }
}
