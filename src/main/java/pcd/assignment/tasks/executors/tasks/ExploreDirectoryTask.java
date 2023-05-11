package pcd.assignment.tasks.executors.tasks;

import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.tasks.strategy.MemorizeStrategy;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ExploreDirectoryTask extends RecursiveTask<Pair<Intervals, LongestFiles>> {
    private final File directory;
    private final MemorizeStrategy strategy;
    private final SourceAnalyzerData data;

    public ExploreDirectoryTask(File directory, SourceAnalyzerData data, MemorizeStrategy strategy) {
        this.directory = directory;
        this.data = data;
        this.strategy = strategy;
    }


    @Override
    protected Pair<Intervals, LongestFiles> compute() {
        List<RecursiveTask<Pair<Intervals, LongestFiles>>> directoryForks = new LinkedList<>();
        List<RecursiveTask<FileInfo>> filesForks = new LinkedList<>();
        exploreAndFork(directoryForks, filesForks);
        joinDirectoriesTask(directoryForks);
        joinReadLinesTasks(filesForks);
        this.strategy.saveResult(this.data.getIntervals(), this.data.getLongestFiles());
        return new Pair<>(this.data.getIntervals(), this.data.getLongestFiles());
    }

    private void joinReadLinesTasks(List<RecursiveTask<FileInfo>> filesForks) {
        for (var task : filesForks) {
            FileInfo fileInfo = task.join();
            this.data.getIntervals().store(fileInfo);
            this.data.getLongestFiles().put(fileInfo);
        }
    }

    private void joinDirectoriesTask(List<RecursiveTask<Pair<Intervals, LongestFiles>>> directoryForks) {
        for (var task : directoryForks) {
            task.join();
        }
    }

    private void exploreAndFork(List<RecursiveTask<Pair<Intervals, LongestFiles>>> directoryForks,
                                List<RecursiveTask<FileInfo>> filesForks) {
        if (this.directory.isDirectory()) {
            File[] files = this.directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        ExploreDirectoryTask task = getExploreDirectoryTask(file);
                        directoryForks.add(task);
                        task.fork();
                    } else {
                        if (file.getName().endsWith(".java")) {
                            ReadLinesTask task = new ReadLinesTask(file);
                            filesForks.add(task);
                            task.fork();
                        }
                    }
                }
            }
        }
    }

    private ExploreDirectoryTask getExploreDirectoryTask(File file) {
        return new ExploreDirectoryTask(
                file,
                this.data,
                this.strategy
        );
    }
}