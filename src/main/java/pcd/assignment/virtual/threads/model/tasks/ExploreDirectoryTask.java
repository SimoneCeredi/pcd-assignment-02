package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.common.utilities.Pair;
import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
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
    private final Intervals lineCounter;
    private final LongestFiles longestFiles;
    private final CompletableFuture<Pair<Intervals, LongestFiles>> future;
    private final BlockingQueue<Thread> threadList;
    private final MemorizeStrategy strategy;

    public ExploreDirectoryTask(
            File directory,
            Intervals lineCounter,
            LongestFiles longestFiles,
            CompletableFuture<Pair<Intervals, LongestFiles>> future,
            BlockingQueue<Thread> threadList,
            MemorizeStrategy strategy
    ) {
        this.directory = directory;
        this.lineCounter = lineCounter;
        this.longestFiles = longestFiles;
        this.future = future;
        this.threadList = threadList;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        List<Future<Pair<Intervals, LongestFiles>>> directoryFutures = new LinkedList<>();
        List<Future<FileInfo>> filesFutures = new LinkedList<>();
        exploreDirectory(directoryFutures, filesFutures);
        collectDirectoryData(directoryFutures);
        collectFilesData(filesFutures);
        this.strategy.saveResult(this.lineCounter, this.longestFiles);
        future.complete(new Pair<>(this.lineCounter, this.longestFiles));
    }

    private void collectFilesData(List<Future<FileInfo>> filesFutures) {
        for (var future : filesFutures) {
            try {
                FileInfo fileInfo = future.get();
                this.lineCounter.store(fileInfo);
                this.longestFiles.put(fileInfo);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void collectDirectoryData(List<Future<Pair<Intervals, LongestFiles>>> directoryFutures) {
        for (var future : directoryFutures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void exploreDirectory(List<Future<Pair<Intervals, LongestFiles>>> directoryFutures, List<Future<FileInfo>> filesFutures) {
        if (this.directory.isDirectory()) {
            File[] files = this.directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        CompletableFuture<Pair<Intervals, LongestFiles>> future = new CompletableFuture<>();
                        ExploreDirectoryTask task = this.getExploreDirectoryTask(file, future);
                        this.threadList.add(Thread.ofVirtual().start(task));
                        directoryFutures.add(future);
                    } else {
                        if (file.getName().endsWith(".java")) {
                            CompletableFuture<FileInfo> future = new CompletableFuture<>();
                            ReadLinesTask task = new ReadLinesTask(file, future);
                            Thread.ofVirtual().start(task);
                            filesFutures.add(future);
                        }
                    }
                }
            }
        }
    }

    private ExploreDirectoryTask getExploreDirectoryTask(File file, CompletableFuture<Pair<Intervals, LongestFiles>> future) {
        return new ExploreDirectoryTask(
                file,
                this.strategy.getChildLineCounter(this.lineCounter),
                this.strategy.getChildLongestFiles(this.longestFiles),
                future,
                this.threadList,
                this.strategy
        );
    }
}
