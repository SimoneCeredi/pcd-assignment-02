package pcd.assignment.tasks.executors.tasks.factory;

import pcd.assignment.common.analyzer.SourceAnalyzerData;
import pcd.assignment.tasks.executors.tasks.ExploreDirectoryTask;

import java.io.File;

public class ExploreDirectoryTaskFactory {

    public ExploreDirectoryTask analyzeSourcesTask(File directory, SourceAnalyzerData data) {
        return new ExploreDirectoryTask(directory, data);
    }

}
