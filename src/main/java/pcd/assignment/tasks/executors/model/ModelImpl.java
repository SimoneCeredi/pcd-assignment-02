package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.*;

public class ModelImpl implements Model {
    private final IntervalLineCounter intervalLineCounter;
    private final LongestFilesQueue longestFiles;

    public ModelImpl(IntervalLineCounter intervalLineCounter, LongestFilesQueue longestFiles) {
        this.intervalLineCounter = intervalLineCounter;
        this.longestFiles = longestFiles;
    }

    @Override
    public Future<Pair<IntervalLineCounter, LongestFilesQueue>> getReport(File directory) {
        CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future = new CompletableFuture<>();
        try (ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)) {
            try {
                if (exec.awaitTermination(10, TimeUnit.MINUTES)) {
                    future.complete(new Pair<>(intervalLineCounter, longestFiles));
                } else {
                    future.cancel(true);
                }
            } catch (InterruptedException e) {
                future.cancel(true);
                throw new RuntimeException(e);
            }
        }
        return future;
    }
}
