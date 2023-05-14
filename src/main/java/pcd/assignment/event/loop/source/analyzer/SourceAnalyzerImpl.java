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
import pcd.assignment.common.source.analyzer.StoppableSourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.event.loop.model.verticles.DirectoryExplorerVerticle;
import pcd.assignment.event.loop.utils.VerticleDeployUtils;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer, StoppableSourceAnalyzer {
    private final Model model;
    private Vertx vertx;
    private volatile boolean stopped = false;
    private Promise<Void> promise;

    public SourceAnalyzerImpl(Model model) {
        this.model = model;
    }


    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory) {
        this.stopped = false;
        this.vertx = Vertx.vertx();
        Intervals intervals = new ConcurrentIntervals(this.model.getNi(), this.model.getMaxl());
        LongestFiles longestFiles = new ConcurrentLongestFiles(this.model.getN());
        BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results = new LinkedBlockingQueue<>();
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        this.promise = Promise.promise();
        vertx.deployVerticle(new DirectoryExplorerVerticle(directory.getAbsolutePath(), promise, intervals, longestFiles, results, this), VerticleDeployUtils.getDeploymentOptions());
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
        this.promise.tryFail("Stopped");
    }

    @Override
    public boolean shouldStop() {
        return this.stopped;
    }
}
