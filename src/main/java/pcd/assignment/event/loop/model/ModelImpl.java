package pcd.assignment.event.loop.model;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import pcd.assignment.event.loop.model.verticles.DirectoryExplorerVerticle;
import pcd.assignment.event.loop.model.verticles.LineCounterVerticle;
import pcd.assignment.tasks.executors.model.AbstractModel;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.Intervals;
import pcd.assignment.tasks.executors.model.data.IntervalsImpl;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class ModelImpl extends AbstractModel {
    private final Vertx vertx = Vertx.vertx();
    private final List<String> deployedVerticles = new LinkedList<>();

    public ModelImpl(int ni, int maxl, int n) {
        super(ni, maxl, n);
        vertx.deployVerticle(new DirectoryExplorerVerticle(), this::evaluateDeployment);
        vertx.deployVerticle(new LineCounterVerticle(), this::evaluateDeployment);
    }

    private void evaluateDeployment(AsyncResult<String> ar) {
        if (ar.succeeded()) {
            this.deployedVerticles.add(ar.result());
        } else {
            System.err.println("Verticle deployment failed");
        }
    }


    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory) {
        BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results = new LinkedBlockingQueue<>();
        Intervals intervals = new IntervalsImpl(this.getNi(), this.getMaxl());
        LongestFiles longestFiles = new LongestFilesImpl(this.getN());
        vertx.eventBus().<JsonObject>consumer("file-info", message -> {
            JsonObject result = message.body();
            FileInfo fileInfo = new FileInfo(new File(result.getString("file-path")), result.getLong("lines"));
            intervals.store(fileInfo);
            longestFiles.put(fileInfo);
            results.add(new Pair<>(intervals.getCopy(), longestFiles.getCopy()));
        });
        vertx.eventBus().send("explore-directory", directory.getAbsolutePath());
        return new Pair<>(results, new CompletableFuture<>());
    }

    @Override
    public void stop() {
        for (String id : this.deployedVerticles) {
            vertx.undeploy(id);
        }
    }
}
