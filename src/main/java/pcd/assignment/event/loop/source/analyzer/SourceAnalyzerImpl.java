package pcd.assignment.event.loop.source.analyzer;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.*;
import pcd.assignment.common.model.data.monitor.ConcurrentLongestFiles;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.source.analyzer.SourceAnalyzerDataImpl;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.event.loop.model.verticles.DirectoryExplorerVerticle;
import pcd.assignment.event.loop.utils.VerticleDeployUtils;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer {

    private final Model model;
    private Vertx vertx;

    public SourceAnalyzerImpl(Model model) {
        this.model = model;
    }

    @Override
    public ResultsData analyzeSources(File directory) {
        this.vertx = Vertx.vertx();
        Intervals intervals = new ConcurrentIntervals(
                this.model.getConfiguration().getNumberOfIntervals(),
                this.model.getConfiguration().getMaximumLines());
        LongestFiles longestFiles = new ConcurrentLongestFiles(this.model.getConfiguration().getAtMostNFiles());
        CompletableFuture<Void> completionFuture = new CompletableFuture<>();
        ResultsData resultsData = new ResultsDataImpl(new LinkedBlockingQueue<>(), completionFuture);

        Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(new DirectoryExplorerVerticle(directory.getAbsolutePath(),
                        promise,
                        new SourceAnalyzerDataImpl(resultsData, intervals, longestFiles)),
                VerticleDeployUtils.getDeploymentOptions());
        promise.future().onComplete(as -> {
            if (as.succeeded()) {
                completionFuture.complete(null);
            } else {
                completionFuture.cancel(true);
            }
            this.vertx.close();
        });
        return resultsData;
    }

}
