package pcd.assignment.tasks.executors.model.tasks.factory;

import pcd.assignment.tasks.executors.model.data.Intervals;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.tasks.executors.model.tasks.ExploreDirectoryTask;
import pcd.assignment.tasks.executors.model.tasks.strategy.AnalyzeSourcesMemorizeStrategyImpl;
import pcd.assignment.utilities.Pair;

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
