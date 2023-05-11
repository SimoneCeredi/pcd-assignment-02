package pcd.assignment.virtual.threads.model.tasks.factory;

import pcd.assignment.common.utilities.Pair;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.tasks.executors.tasks.strategy.AnalyzeSourcesMemorizeStrategyImpl;
import pcd.assignment.virtual.threads.model.tasks.ExploreDirectoryTask;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class ExploreDirectoryTaskFactory {

    public ExploreDirectoryTask analyzeSourcesTask(
            File directory,
            Intervals lineCounter,
            LongestFiles longestFiles,
            CompletableFuture<Pair<Intervals, LongestFiles>> future,
            BlockingQueue<Thread> threadList,
            BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results
    ) {
        return new ExploreDirectoryTask(directory, lineCounter, longestFiles, future, threadList, new AnalyzeSourcesMemorizeStrategyImpl(results));
    }
}
