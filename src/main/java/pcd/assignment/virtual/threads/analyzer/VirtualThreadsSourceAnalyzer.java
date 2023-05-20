package pcd.assignment.virtual.threads.analyzer;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.functions.ConcurrentIntervals;
import pcd.assignment.common.model.data.functions.ConcurrentLongestFiles;
import pcd.assignment.common.model.data.functions.Intervals;
import pcd.assignment.common.model.data.functions.LongestFiles;
import pcd.assignment.common.model.data.results.Result;
import pcd.assignment.common.model.data.results.ResultsData;
import pcd.assignment.common.model.data.results.ResultsDataImpl;
import pcd.assignment.common.analyzer.SourceAnalyzer;
import pcd.assignment.common.analyzer.SourceAnalyzerDataImpl;
import pcd.assignment.virtual.threads.model.tasks.factory.ExploreDirectoryTaskFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer {

    private final ExploreDirectoryTaskFactory factory;
    private final Model model;


    public SourceAnalyzerImpl(Model model) {
        this.model = model;
        this.factory = new ExploreDirectoryTaskFactory();
    }

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

        Thread.ofVirtual().start(
                this.factory.analyzeSourcesTask(
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
