package pcd.assignment.impl.virtualthreads.analyzer;

import pcd.assignment.base.model.Model;
import pcd.assignment.base.model.data.functions.ConcurrentIntervals;
import pcd.assignment.base.model.data.functions.ConcurrentLongestFiles;
import pcd.assignment.base.model.data.functions.Intervals;
import pcd.assignment.base.model.data.functions.LongestFiles;
import pcd.assignment.base.model.data.results.Result;
import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.base.model.data.results.ResultsDataImpl;
import pcd.assignment.base.analyzer.SourceAnalyzer;
import pcd.assignment.base.analyzer.SourceAnalyzerDataImpl;
import pcd.assignment.impl.virtualthreads.model.tasks.ExploreDirectoryTask;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Virtual threads-based version of SourceAnalyzer.
 */
public class VirtualThreadsSourceAnalyzer implements SourceAnalyzer {

    private final Model model;


    public VirtualThreadsSourceAnalyzer(Model model) {
        this.model = model;
    }

    /**
     * A new Virtual thread (ExploreDirectoryTask) is run, passing a CompletableFuture to it.
     * When the Result is ready it completes the ResultsData's CompletableFuture.
     * @param directory where to start the computation
     * @return ResultsData
     */
    @Override
    public ResultsData analyzeSources(File directory) {
        BlockingQueue<Result> results = new LinkedBlockingQueue<>();
        Intervals intervals = new ConcurrentIntervals(
                this.model.getConfiguration().getNumberOfIntervals(),
                this.model.getConfiguration().getMaximumLines());
        LongestFiles longestFiles = new ConcurrentLongestFiles(this.model.getConfiguration().getAtMostNFiles());

        CompletableFuture<Result> rootDirFuture = new CompletableFuture<>();
        CompletableFuture<Void> completionFuture = new CompletableFuture<>();

        ResultsData resultsData = new ResultsDataImpl(results, completionFuture);

        // Start a virtual thread
        Thread.ofVirtual().start(
                new ExploreDirectoryTask(
                        directory,
                        rootDirFuture,
                        new SourceAnalyzerDataImpl(resultsData, intervals, longestFiles)
                )
        );
        completionFuture.completeAsync(() -> {
            try {
                rootDirFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        return new ResultsDataImpl(results, completionFuture);
    }

}
