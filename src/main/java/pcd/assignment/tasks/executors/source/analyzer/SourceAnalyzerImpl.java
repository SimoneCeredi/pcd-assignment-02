package pcd.assignment.tasks.executors.source.analyzer;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.*;
import pcd.assignment.common.model.data.monitor.ConcurrentLongestFiles;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.tasks.factory.ExploreDirectoryTaskFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer, SourceAnalyzerData {
    private final ExploreDirectoryTaskFactory factory = new ExploreDirectoryTaskFactory();
    private final Model model;
    private ForkJoinPool analyzeSourcesPool;
    private BlockingQueue<Result> results;
    private Intervals intervals;
    private LongestFiles longestFiles;
    private volatile boolean stopped = false;

    public SourceAnalyzerImpl(Model model) {
        this.model = model;
    }

    @Override
    public ResultsData analyzeSources(File directory) {
        this.stopped = false;
        this.results = new LinkedBlockingQueue<>();
        this.analyzeSourcesPool = new ForkJoinPool();
        this.intervals = new ConcurrentIntervals(this.model.getNumberOfIntervals(), this.model.getMaximumLines());
        this.longestFiles = new ConcurrentLongestFiles(this.model.getAtMostNFiles());
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            this.analyzeSourcesPool.invoke(factory.analyzeSourcesTask(directory, this));
            return null;
        });
        return new ResultsDataImpl(results, future);
    }

    /*@Override
    public void stop() {
        this.stopped = true;
        if (this.analyzeSourcesPool != null && !this.analyzeSourcesPool.isTerminated()) {
            this.analyzeSourcesPool.shutdownNow();
        }
    }*/

    @Override
    public BlockingQueue<Result> getResults() {
        return this.results;
    }

    @Override
    public Intervals getIntervals() {
        return this.intervals;
    }

    @Override
    public LongestFiles getLongestFiles() {
        return this.longestFiles;
    }

    @Override
    public boolean shouldStop() {
        return this.stopped;
    }
}
