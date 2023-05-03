package pcd.assignment.tasks.executors.model;

import pcd.assignment.model.AbstractModel;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounterImpl;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueueImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.tasks.executors.model.tasks.factory.ExploreDirectoryTaskFactory;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class ModelImpl extends AbstractModel {
    private final ExploreDirectoryTaskFactory factory = new ExploreDirectoryTaskFactory();
    private ForkJoinPool analyzeSourcesPool;


    public ModelImpl(int ni, int maxl, int n) {
        super(ni, maxl, n);
    }


    @Override
    public CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> getReport(File directory) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> ret = new CompletableFuture<>();
        ret.completeAsync(() -> {
            Pair<IntervalLineCounter, LongestFilesQueue> i = forkJoinPool.invoke(
                    factory.getReportTask(
                            directory,
                            new IntervalLineCounterImpl(this.getNi(), this.getMaxl()),
                            new LongestFilesQueueImpl(this.getN())
                    )
            );
            return new Pair<>(i.getX(), i.getY());
        });
        return ret;
    }

    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>>, CompletableFuture<Void>> analyzeSources(File directory) {
        BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> results = new LinkedBlockingQueue<>();
        this.analyzeSourcesPool = new ForkJoinPool();
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            this.analyzeSourcesPool.invoke(
                    factory.analyzeSourcesTask(
                            directory,
                            new IntervalLineCounterImpl(this.getNi(), this.getMaxl()),
                            new LongestFilesQueueImpl(this.getN()),
                            results)
            );
            return null;
        });
        return new Pair<>(results, future);
    }

    @Override
    public void stop() {
        if (this.analyzeSourcesPool != null && !this.analyzeSourcesPool.isTerminated()) {
            this.analyzeSourcesPool.shutdownNow();
        }
    }

}
