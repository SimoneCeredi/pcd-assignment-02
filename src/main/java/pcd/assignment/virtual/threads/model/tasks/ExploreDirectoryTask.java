package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.tasks.strategy.MemorizeStrategy;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExploreDirectoryTask implements Runnable {
    private final File directory;
    private final IntervalLineCounter lineCounter;
    private final LongestFilesQueue longestFiles;
    private final CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future;
    private final MemorizeStrategy strategy;

    public ExploreDirectoryTask(
            File directory,
            IntervalLineCounter lineCounter,
            LongestFilesQueue longestFiles,
            CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future,
            MemorizeStrategy strategy
    ) {
        this.directory = directory;
        this.lineCounter = lineCounter;
        this.longestFiles = longestFiles;
        this.future = future;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        List<Future<Pair<IntervalLineCounter, LongestFilesQueue>>> directoryFutures = new LinkedList<>();
        List<Future<FileInfo>> filesFutures = new LinkedList<>();
        exploreDirectory(directoryFutures, filesFutures);
        collectDirectoryData(directoryFutures);
        collectFilesData(filesFutures);
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

    private void collectDirectoryData(List<Future<Pair<IntervalLineCounter, LongestFilesQueue>>> directoryFutures) {
        for (var future : directoryFutures) {
            try {
                var values = future.get();
                this.strategy.storeSubResult(this.lineCounter, this.longestFiles, values);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void exploreDirectory(List<Future<Pair<IntervalLineCounter, LongestFilesQueue>>> directoryFutures, List<Future<FileInfo>> filesFutures) {
        if (this.directory.isDirectory()) {
            File[] files = this.directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future = new CompletableFuture<>();
                        ExploreDirectoryTask task = this.getExploreDirectoryTask(file, future);
                        Thread.ofVirtual().start(task);
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

    private ExploreDirectoryTask getExploreDirectoryTask(File file, CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future) {
        return new ExploreDirectoryTask(
                file,
                this.strategy.getChildLineCounter(this.lineCounter),
                this.strategy.getChildLongestFiles(this.longestFiles),
                future,
                this.strategy
        );
    }
}
