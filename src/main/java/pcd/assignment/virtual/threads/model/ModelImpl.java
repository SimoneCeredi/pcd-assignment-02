package pcd.assignment.virtual.threads.model;

import pcd.assignment.model.AbstractModel;
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
import java.util.concurrent.ForkJoinTask;

public class ModelImpl extends AbstractModel {
    private final ExploreDirectoryTaskFactory factory = new ExploreDirectoryTaskFactory();

    public ModelImpl(int ni, int maxl, int n) {
        super(ni, maxl, n);
    }

    @Override
    public CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> getReport(File directory) {
        CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future = new CompletableFuture<>();
        Thread.ofVirtual().start(
                factory.getReportTask(
                        directory,
                        new IntervalLineCounterImpl(this.getNi(), this.getMaxl()),
                        new LongestFilesQueueImpl(this.getN()),
                        future
                )
        );
        CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> ret = new CompletableFuture<>();
        return ret.completeAsync(() -> {
            try {
                var res = future.get();
                return new Pair<>(res.getX(), res.getY());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>>, ForkJoinTask<Pair<IntervalLineCounter, LongestFilesQueue>>> analyzeSources(File directory) {
        return null;
    }

    @Override
    public void stop() {

    }
}
