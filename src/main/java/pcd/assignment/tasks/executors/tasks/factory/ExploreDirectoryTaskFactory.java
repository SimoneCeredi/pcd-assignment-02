package pcd.assignment.tasks.executors.tasks.factory;

import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.tasks.executors.tasks.ExploreDirectoryTask;
import pcd.assignment.tasks.executors.tasks.strategy.AnalyzeSourcesMemorizeStrategyImpl;

import java.io.File;

public class ExploreDirectoryTaskFactory {
    public ExploreDirectoryTask analyzeSourcesTask(File directory, SourceAnalyzerData data) {
        return new ExploreDirectoryTask(directory, data, new AnalyzeSourcesMemorizeStrategyImpl(data.getResults()));
    }
}
