package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.IntervalsImpl;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.model.data.monitor.ConcurrentLongestFiles;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFiles;
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
    public Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory) {
        BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results = new LinkedBlockingQueue<>();
        this.analyzeSourcesPool = new ForkJoinPool();
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            this.analyzeSourcesPool.invoke(
                    factory.analyzeSourcesTask(
                            directory,
                            new IntervalsImpl(this.getNi(), this.getMaxl()),
                            new ConcurrentLongestFiles(this.getN()),
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
