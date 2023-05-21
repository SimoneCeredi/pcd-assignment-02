package pcd.assignment.tasks.executors.analyzer;

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
import pcd.assignment.tasks.executors.tasks.ExploreDirectoryTask;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Task-based version of SourceAnalyzer.
 */
public class TaskExecutorsSourceAnalyzer implements SourceAnalyzer {

    private final Model model;
    private ForkJoinPool analyzeSourcesPool;

    public TaskExecutorsSourceAnalyzer(Model model) {
        this.model = model;
    }

    /**
     *  - Create an ExecutorService for running ForkJoinTasks
     *  - Invoke an ExploreDirectoryTask on it from the directory passed as parameter
     *  - Complete ResultsData's CompletableFuture upon completion.
     * @param directory
     * @return ResultsData
     */
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
            this.analyzeSourcesPool.invoke(new ExploreDirectoryTask(directory,
                    new SourceAnalyzerDataImpl(resultsData, intervals, longestFiles)));
            return null;
        });
        return resultsData;
    }

}
