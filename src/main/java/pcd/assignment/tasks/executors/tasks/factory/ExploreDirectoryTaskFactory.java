package pcd.assignment.tasks.executors.tasks.factory;

import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.Intervals;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.tasks.executors.tasks.ExploreDirectoryTask;
import pcd.assignment.tasks.executors.tasks.strategy.AnalyzeSourcesMemorizeStrategyImpl;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class ExploreDirectoryTaskFactory {
    public ExploreDirectoryTask analyzeSourcesTask(
            File directory,
            Intervals lineCounter,
            LongestFiles longestFiles,
            BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results
    ) {
        return new ExploreDirectoryTask(directory, lineCounter, longestFiles, new AnalyzeSourcesMemorizeStrategyImpl(results));
    }
}
