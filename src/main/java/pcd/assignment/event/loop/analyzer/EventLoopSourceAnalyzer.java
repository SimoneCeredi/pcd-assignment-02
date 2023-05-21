package pcd.assignment.event.loop.analyzer;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.functions.ConcurrentIntervals;
import pcd.assignment.common.model.data.functions.ConcurrentLongestFiles;
import pcd.assignment.common.model.data.functions.Intervals;
import pcd.assignment.common.model.data.functions.LongestFiles;
import pcd.assignment.common.model.data.results.ResultsData;
import pcd.assignment.common.model.data.results.ResultsDataImpl;
import pcd.assignment.common.analyzer.SourceAnalyzer;
import pcd.assignment.common.analyzer.SourceAnalyzerDataImpl;
import pcd.assignment.event.loop.model.verticles.DirectoryExplorerVerticle;
import pcd.assignment.event.loop.utils.VerticleDeployUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class EventLoopSourceAnalyzer implements SourceAnalyzer {

    private final Model model;
    private Vertx vertx;

    public EventLoopSourceAnalyzer(Model model) {
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
        vertx.deployVerticle(new DirectoryExplorerVerticle(directory.getAbsolutePath(), promise,
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
