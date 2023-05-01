package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ExploreDirectoryTaskFactory {
    public ExploreDirectoryTask getReportTask(File directory, IntervalLineCounter lineCounter, LongestFilesQueue longestFiles) {
        return new ExploreDirectoryTask(directory, lineCounter, longestFiles, new GetReportMemorizeStrategyImpl());
    }

    public ExploreDirectoryTask analyzeSourcesTask(
            File directory,
            IntervalLineCounter lineCounter,
            LongestFilesQueue longestFiles,
            BlockingQueue<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> results
    ) {
        return new ExploreDirectoryTask(directory, lineCounter, longestFiles, new AnalyzeSourcesMemorizeStrategyImpl(results));
    }
}
