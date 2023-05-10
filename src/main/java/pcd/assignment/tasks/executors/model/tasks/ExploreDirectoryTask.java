package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.Intervals;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.model.tasks.strategy.MemorizeStrategy;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ExploreDirectoryTask extends RecursiveTask<Pair<Intervals, LongestFiles>> {
    private final File directory;
    private final Intervals lineCounter;
    private final LongestFiles longestFiles;
    private final MemorizeStrategy strategy;

    public ExploreDirectoryTask(
            File directory,
            Intervals lineCounter,
            LongestFiles longestFiles,
            MemorizeStrategy strategy
    ) {
        this.directory = directory;
        this.lineCounter = lineCounter;
        this.longestFiles = longestFiles;
        this.strategy = strategy;
    }


    @Override
    protected Pair<Intervals, LongestFiles> compute() {
        List<RecursiveTask<Pair<Intervals, LongestFiles>>> directoryForks = new LinkedList<>();
        List<RecursiveTask<FileInfo>> filesForks = new LinkedList<>();
        exploreAndFork(directoryForks, filesForks);
        joinDirectoriesTask(directoryForks);
        joinReadLinesTasks(filesForks);
        this.strategy.saveResult(this.lineCounter, this.longestFiles);
        return new Pair<>(this.lineCounter, this.longestFiles);
    }

    private void joinReadLinesTasks(List<RecursiveTask<FileInfo>> filesForks) {
        for (var task : filesForks) {
            FileInfo fileInfo = task.join();
            this.lineCounter.store(fileInfo);
            this.longestFiles.put(fileInfo);
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
                this.strategy.getChildLineCounter(this.lineCounter),
                this.strategy.getChildLongestFiles(this.longestFiles),
                this.strategy
        );
    }
}