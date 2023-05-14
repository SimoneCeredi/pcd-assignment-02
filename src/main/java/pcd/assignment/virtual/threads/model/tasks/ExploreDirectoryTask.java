package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
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
    private final SourceAnalyzerData data;
    private final MemorizeStrategy strategy;
    private final BlockingQueue<Thread> threads;

    public ExploreDirectoryTask(File directory, CompletableFuture<Pair<Intervals, LongestFiles>> future, SourceAnalyzerData data, MemorizeStrategy strategy, BlockingQueue<Thread> threads) {
        this.directory = directory;
        this.future = future;
        this.data = data;
        this.strategy = strategy;
        this.threads = threads;
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
        if (!this.data.shouldStop()) {
            this.strategy.saveResult(this.data.getIntervals(), this.data.getLongestFiles());
        }
        future.complete(new Pair<>(this.data.getIntervals(), this.data.getLongestFiles()));
    }

    private void collectFilesData(List<Future<FileInfo>> filesFutures) {
        for (var future : filesFutures) {
            try {
                FileInfo fileInfo = future.get();
                this.data.getIntervals().store(fileInfo);
                this.data.getLongestFiles().put(fileInfo);
            } catch (InterruptedException ignored) {
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void collectDirectoryData(List<Future<Pair<Intervals, LongestFiles>>> directoryFutures) {
        for (var future : directoryFutures) {
            try {
                future.get();
            } catch (InterruptedException ignored) {
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void exploreDirectory(List<Future<Pair<Intervals, LongestFiles>>> directoryFutures, List<Future<FileInfo>> filesFutures) throws InterruptedException {
        if (this.directory.isDirectory()) {
            File[] files = this.directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!this.data.shouldStop()) {
                        if (file.isDirectory()) {
                            CompletableFuture<Pair<Intervals, LongestFiles>> future = new CompletableFuture<>();
                            this.threads.put(Thread.ofVirtual().start(this.getExploreDirectoryTask(file, future)));
                            directoryFutures.add(future);
                        } else {
                            if (file.getName().endsWith(".java")) {
                                CompletableFuture<FileInfo> future = new CompletableFuture<>();
                                ReadLinesTask task = new ReadLinesTask(file, future, data);
                                this.threads.put(Thread.ofVirtual().start(task));
                                filesFutures.add(future);
                            }
                        }
                    }
                }
            }
        }
    }

    private ExploreDirectoryTask getExploreDirectoryTask(File file, CompletableFuture<Pair<Intervals, LongestFiles>> future) {
        return new ExploreDirectoryTask(file, future, this.data, this.strategy, threads);
    }
}
