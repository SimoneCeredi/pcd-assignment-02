package pcd.assignment.virtual.threads.model.tasks.factory;

import pcd.assignment.common.model.data.results.Result;
import pcd.assignment.common.analyzer.SourceAnalyzerData;
import pcd.assignment.virtual.threads.model.tasks.ExploreDirectoryTask;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class ExploreDirectoryTaskFactory {

    public ExploreDirectoryTask analyzeSourcesTask(File directory,
                                                   CompletableFuture<Result> tasksFuture,
                                                   SourceAnalyzerData data) {
        return new ExploreDirectoryTask(directory, tasksFuture, data);
    }
}
