package pcd.assignment.virtual.threads.model.tasks.factory;

import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.tasks.strategy.AnalyzeSourcesMemorizeStrategyImpl;
import pcd.assignment.virtual.threads.model.tasks.ExploreDirectoryTask;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ExploreDirectoryTaskFactory {

    public ExploreDirectoryTask analyzeSourcesTask(File directory, CompletableFuture<Pair<Intervals, LongestFiles>> future, SourceAnalyzerData data) {
        return new ExploreDirectoryTask(directory, future, data, new AnalyzeSourcesMemorizeStrategyImpl(data.getResults()));
    }
}
