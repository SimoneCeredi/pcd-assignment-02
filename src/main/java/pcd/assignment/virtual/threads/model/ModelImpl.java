package pcd.assignment.virtual.threads.model;

import pcd.assignment.tasks.executors.model.AbstractModel;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounterImpl;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueueImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.utilities.Pair;
import pcd.assignment.virtual.threads.model.tasks.factory.ExploreDirectoryTaskFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

public class ModelImpl extends AbstractModel {
    private final ExploreDirectoryTaskFactory factory = new ExploreDirectoryTaskFactory();
    private Thread analyzeSourcesThread;

    public ModelImpl(int ni, int maxl, int n) {
        super(ni, maxl, n);
    }

    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>>, CompletableFuture<Void>> analyzeSources(File directory) {
        BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> results = new LinkedBlockingQueue<>();
        CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future = new CompletableFuture<>();
        CompletableFuture<Void> ret = new CompletableFuture<>();
        this.analyzeSourcesThread = Thread.ofVirtual().start(
                this.factory.analyzeSourcesTask(
                        directory,
                        new IntervalLineCounterImpl(this.getNi(), this.getMaxl()),
                        new LongestFilesQueueImpl(this.getN()),
                        future,
                        results
                )
        );
        ret.completeAsync(() -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        return new Pair<>(results, ret);
    }

    @Override
    public void stop() {
        this.analyzeSourcesThread.interrupt();
    }
}
