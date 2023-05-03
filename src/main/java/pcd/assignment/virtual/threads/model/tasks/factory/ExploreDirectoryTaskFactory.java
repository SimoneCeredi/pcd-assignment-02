package pcd.assignment.virtual.threads.model.tasks.factory;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.tasks.executors.model.tasks.strategy.AnalyzeSourcesMemorizeStrategyImpl;
import pcd.assignment.tasks.executors.model.tasks.strategy.GetReportMemorizeStrategyImpl;
import pcd.assignment.utilities.Pair;
import pcd.assignment.virtual.threads.model.tasks.ExploreDirectoryTask;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class ExploreDirectoryTaskFactory {
    public ExploreDirectoryTask getReportTask(
            File directory,
            IntervalLineCounter lineCounter,
            LongestFilesQueue longestFiles,
            CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future
    ) {
        return new ExploreDirectoryTask(directory, lineCounter, longestFiles, future, new GetReportMemorizeStrategyImpl());
    }

    public ExploreDirectoryTask analyzeSourcesTask(
            File directory,
            IntervalLineCounter lineCounter,
            LongestFilesQueue longestFiles,
            CompletableFuture<Pair<IntervalLineCounter, LongestFilesQueue>> future,
            BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> results
    ) {
        return new ExploreDirectoryTask(directory, lineCounter, longestFiles, future, new AnalyzeSourcesMemorizeStrategyImpl(results));
    }
}
