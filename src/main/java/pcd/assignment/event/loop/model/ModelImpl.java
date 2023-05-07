package pcd.assignment.event.loop.model;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import pcd.assignment.event.loop.model.verticles.DirectoryExplorerVerticle;
import pcd.assignment.event.loop.utils.VerticleDeployUtils;
import pcd.assignment.tasks.executors.model.AbstractModel;
import pcd.assignment.tasks.executors.model.data.Intervals;
import pcd.assignment.tasks.executors.model.data.IntervalsImpl;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class ModelImpl extends AbstractModel implements ModelData {
    private final Vertx vertx = Vertx.vertx();
    private final BlockingQueue<String> deployedVerticles = new LinkedBlockingQueue<>();
    private BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results;
    private Intervals intervals;
    private LongestFiles longestFiles;

    public ModelImpl(int ni, int maxl, int n) {
        super(ni, maxl, n);
    }


    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory) {
        this.intervals = new IntervalsImpl(this.getNi(), this.getMaxl());
        this.longestFiles = new LongestFilesImpl(this.getN());
        this.results = new LinkedBlockingQueue<>();

        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(
                new DirectoryExplorerVerticle(directory.getAbsolutePath(), promise, deployedVerticles, this),
                ar -> VerticleDeployUtils.evaluateDeployment(ar, this.deployedVerticles)
        );
        promise.future().onComplete(as -> {
            if (as.succeeded()) {
                completableFuture.complete(null);
            } else {
                completableFuture.cancel(true);
            }
        });
        return new Pair<>(results, completableFuture);
    }

    @Override
    public void stop() {
        for (String id : this.deployedVerticles) {
            vertx.undeploy(id);
        }
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
