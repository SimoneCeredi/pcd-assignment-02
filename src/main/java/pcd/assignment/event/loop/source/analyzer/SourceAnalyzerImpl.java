package pcd.assignment.event.loop.source.analyzer;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.ConcurrentIntervals;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.ConcurrentLongestFiles;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.event.loop.model.verticles.DirectoryExplorerVerticle;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer, SourceAnalyzerData {
    private final Model model;
    private Vertx vertx;
    private BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results;
    private Intervals intervals;
    private LongestFiles longestFiles;
    private volatile boolean stopped = false;

    public SourceAnalyzerImpl(Model model) {
        this.model = model;
    }


    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory) {
        this.stopped = false;
        this.vertx = Vertx.vertx();
        this.intervals = new ConcurrentIntervals(this.model.getNi(), this.model.getMaxl());
        this.longestFiles = new ConcurrentLongestFiles(this.model.getN());
        this.results = new LinkedBlockingQueue<>();

        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(new DirectoryExplorerVerticle(directory.getAbsolutePath(), promise, this));
        promise.future().onComplete(as -> {
            if (as.succeeded()) {
                completableFuture.complete(null);
            } else {
                completableFuture.cancel(true);
            }
            this.vertx.close();
        });
        return new Pair<>(results, completableFuture);
    }

    @Override
    public void stop() {
        this.stopped = true;
        this.vertx.close();
    }

    @Override
    public boolean shouldStop() {
        return this.stopped;
    }

    @Override
    public BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> getResults() {
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
}
