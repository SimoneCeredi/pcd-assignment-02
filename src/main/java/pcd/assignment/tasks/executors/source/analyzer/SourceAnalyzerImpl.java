package pcd.assignment.tasks.executors.source.analyzer;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.IntervalsImpl;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.tasks.executors.data.monitor.ConcurrentLongestFiles;
import pcd.assignment.tasks.executors.tasks.factory.ExploreDirectoryTaskFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer {
    private final ExploreDirectoryTaskFactory factory = new ExploreDirectoryTaskFactory();
    private final Model model;
    private ForkJoinPool analyzeSourcesPool;

    public SourceAnalyzerImpl(Model model) {
        this.model = model;
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
                            new IntervalsImpl(this.model.getNi(), this.model.getMaxl()),
                            new ConcurrentLongestFiles(this.model.getN()),
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
