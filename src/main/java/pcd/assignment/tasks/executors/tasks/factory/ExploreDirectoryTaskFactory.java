package pcd.assignment.tasks.executors.tasks.factory;

import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.Result;
import pcd.assignment.common.model.data.ResultsData;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.tasks.executors.tasks.ExploreDirectoryTask;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ExploreDirectoryTaskFactory {

    public ExploreDirectoryTask analyzeSourcesTask(File directory, SourceAnalyzerData data) {
        return new ExploreDirectoryTask(directory, data);
    }

}
