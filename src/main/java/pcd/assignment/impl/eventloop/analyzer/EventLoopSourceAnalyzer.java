package pcd.assignment.impl.eventloop.analyzer;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import pcd.assignment.base.model.Model;
import pcd.assignment.base.model.data.functions.ConcurrentIntervals;
import pcd.assignment.base.model.data.functions.ConcurrentLongestFiles;
import pcd.assignment.base.model.data.functions.Intervals;
import pcd.assignment.base.model.data.functions.LongestFiles;
import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.base.model.data.results.ResultsDataImpl;
import pcd.assignment.base.analyzer.SourceAnalyzer;
import pcd.assignment.base.analyzer.SourceAnalyzerDataImpl;
import pcd.assignment.impl.eventloop.model.verticles.DirectoryExplorerVerticle;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Event loop version of SourceAnalyzer.
 */
public class EventLoopSourceAnalyzer implements SourceAnalyzer {

    private final Model model;
    private Vertx vertx;

    public EventLoopSourceAnalyzer(Model model) {
        this.model = model;
    }

    /**
     * Deploys a DirectoryExplorerVerticle on a Vertx instance on
     * the directory given as parameter.
     * A Promise is also passed to it so when it completes, the ResultsData future
     * is also set completed.
     * @param directory where to start the computation
     * @return ResultsData object
     */
    @Override
    public ResultsData analyzeSources(File directory) {
        this.vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(1));

        Intervals intervals = new ConcurrentIntervals(
                this.model.getConfiguration().getNumberOfIntervals(),
                this.model.getConfiguration().getMaximumLines());
        LongestFiles longestFiles = new ConcurrentLongestFiles(this.model.getConfiguration().getAtMostNFiles());
        CompletableFuture<Void> completionFuture = new CompletableFuture<>();
        ResultsData resultsData = new ResultsDataImpl(new LinkedBlockingQueue<>(), completionFuture);

        Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(new DirectoryExplorerVerticle(directory.getAbsolutePath(), promise,
                        new SourceAnalyzerDataImpl(resultsData, intervals, longestFiles)));
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
