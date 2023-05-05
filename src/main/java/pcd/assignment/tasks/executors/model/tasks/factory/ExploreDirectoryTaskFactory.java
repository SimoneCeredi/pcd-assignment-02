package pcd.assignment.tasks.executors.model.tasks.factory;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.tasks.executors.model.tasks.ExploreDirectoryTask;
import pcd.assignment.tasks.executors.model.tasks.strategy.AnalyzeSourcesMemorizeStrategyImpl;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class ExploreDirectoryTaskFactory {
    public ExploreDirectoryTask analyzeSourcesTask(
            File directory,
            IntervalLineCounter lineCounter,
            LongestFilesQueue longestFiles,
            BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> results
    ) {
        return new ExploreDirectoryTask(directory, lineCounter, longestFiles, new AnalyzeSourcesMemorizeStrategyImpl(results));
    }
}
