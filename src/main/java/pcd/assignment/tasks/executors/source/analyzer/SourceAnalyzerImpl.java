package pcd.assignment.tasks.executors.source.analyzer;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.*;
import pcd.assignment.common.model.data.monitor.ConcurrentLongestFiles;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.source.analyzer.SourceAnalyzerDataImpl;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.tasks.factory.ExploreDirectoryTaskFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer {
    private final ExploreDirectoryTaskFactory factory;
    private final Model model;
    private ForkJoinPool analyzeSourcesPool;


    public SourceAnalyzerImpl(Model model) {
        this.model = model;
        this.factory = new ExploreDirectoryTaskFactory();
    }

    @Override
    public ResultsData analyzeSources(File directory) {
        BlockingQueue<Result> results = new LinkedBlockingQueue<>();
        this.analyzeSourcesPool = new ForkJoinPool();

        Intervals intervals = new ConcurrentIntervals(
                this.model.getConfiguration().getNumberOfIntervals(),
                this.model.getConfiguration().getMaximumLines());
        LongestFiles longestFiles = new ConcurrentLongestFiles(this.model.getConfiguration().getAtMostNFiles());
        CompletableFuture<Void> completionFuture = new CompletableFuture<>();
        ResultsData resultsData = new ResultsDataImpl(results, completionFuture);

        completionFuture.completeAsync(() -> {
            this.analyzeSourcesPool.invoke(factory.analyzeSourcesTask(directory,
                    new SourceAnalyzerDataImpl(resultsData, intervals, longestFiles)));
            return null;
        });

        return resultsData;
    }

}
