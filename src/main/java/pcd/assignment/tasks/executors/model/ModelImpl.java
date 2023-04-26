package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.tasks.ExploreDirectoryTask;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.*;

public class ModelImpl implements Model, TasksModel {
    private final IntervalLineCounter intervalLineCounter;
    private final LongestFilesQueue longestFiles;
    private final ExecutorService executorService;

    public ModelImpl(IntervalLineCounter intervalLineCounter, LongestFilesQueue longestFiles) {
        this.intervalLineCounter = intervalLineCounter;
        this.longestFiles = longestFiles;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    @Override
    public IntervalLineCounter getIntervalLineCounter() {
        return intervalLineCounter;
    }

    @Override
    public LongestFilesQueue getLongestFiles() {
        return longestFiles;
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public Future<Pair<IntervalLineCounter, LongestFilesQueue>> getReport(File directory) {
        CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future = new CompletableFuture<>();
        this.executorService.submit(new ExploreDirectoryTask(directory, this));

        try {
            if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                future.complete(new Pair<>(intervalLineCounter, longestFiles));
            } else {
                future.cancel(true);
            }
        } catch (InterruptedException e) {
            future.cancel(true);
            throw new RuntimeException(e);
        }

        return future;
    }
}
