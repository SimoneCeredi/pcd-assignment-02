package pcd.assignment.virtual.threads.source.analyzer;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.*;
import pcd.assignment.common.model.data.monitor.ConcurrentLongestFiles;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.virtual.threads.model.tasks.factory.ExploreDirectoryTaskFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer, SourceAnalyzerData {

    private final ExploreDirectoryTaskFactory factory = new ExploreDirectoryTaskFactory();
    private final Model model;
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
        this.intervals = new ConcurrentIntervals(
                this.model.getConfiguration().getNumberOfIntervals(),
                this.model.getConfiguration().getMaximumLines());
        this.longestFiles = new ConcurrentLongestFiles(this.model.getConfiguration().getAtMostNFiles());
        CompletableFuture<Pair<Intervals, LongestFiles>> future = new CompletableFuture<>();
        CompletableFuture<Void> ret = new CompletableFuture<>();
        Thread.ofVirtual().start(
                this.factory.analyzeSourcesTask(
                        directory,
                        future,
                        this
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
        return new ResultsDataImpl(results, ret);
    }

    /*@Override
    public void stop() {
        this.stopped = true;
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
