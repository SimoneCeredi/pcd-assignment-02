package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounterImpl;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueueImpl;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RecursiveTask;

public class ExploreDirectoryTask extends RecursiveTask<Pair<IntervalLineCounter, LongestFilesQueue>> {
    private final File directory;
    private final IntervalLineCounter lineCounter;
    private final LongestFilesQueue longestFiles;
    private final BlockingQueue<Pair<IntervalLineCounter, LongestFilesQueue>> results;

    public ExploreDirectoryTask(
            File directory,
            IntervalLineCounter lineCounter,
            LongestFilesQueue longestFiles,
            BlockingQueue<Pair<IntervalLineCounter, LongestFilesQueue>> results
    ) {
        this.directory = directory;
        this.lineCounter = lineCounter;
        this.longestFiles = longestFiles;
        this.results = results;
    }

    public ExploreDirectoryTask(File directory, IntervalLineCounter lineCounter, LongestFilesQueue longestFiles) {
        this(directory, lineCounter, longestFiles, null);
    }


    @Override
    protected Pair<IntervalLineCounter, LongestFilesQueue> compute() {
        List<RecursiveTask<Pair<IntervalLineCounter, LongestFilesQueue>>> directoryForks = new LinkedList<>();
        List<RecursiveTask<FileInfo>> filesForks = new LinkedList<>();
        exploreAndFork(directoryForks, filesForks);
        joinDirectoriesTask(directoryForks);
        joinReadLinesTasks(filesForks);
        if (this.results != null) {
            try {
                this.results.put(new Pair<>(this.lineCounter.getCopy(), this.longestFiles.getCopy()));
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return new Pair<>(this.lineCounter, this.longestFiles);
    }

    private void joinReadLinesTasks(List<RecursiveTask<FileInfo>> filesForks) {
        for (var task : filesForks) {
            FileInfo fileInfo = task.join();
            this.lineCounter.store(fileInfo);
            this.longestFiles.put(fileInfo);
        }
    }

    private void joinDirectoriesTask(List<RecursiveTask<Pair<IntervalLineCounter, LongestFilesQueue>>> directoryForks) {
        for (var task : directoryForks) {
            var values = task.join();
            if (this.results == null) {
                this.lineCounter.storeAll(values.getX());
                this.longestFiles.putAll(values.getY());
            }
        }
    }

    private void exploreAndFork(List<RecursiveTask<Pair<IntervalLineCounter, LongestFilesQueue>>> directoryForks, List<RecursiveTask<FileInfo>> filesForks) {
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

        if (this.results == null) {

            return new ExploreDirectoryTask(file,
                    new IntervalLineCounterImpl(this.lineCounter.getIntervals(), this.lineCounter.getMaxLines()),
                    new LongestFilesQueueImpl(this.longestFiles.getFilesToKeep())
            );
        } else {
            return new ExploreDirectoryTask(file,
                    this.lineCounter,
                    this.longestFiles,
                    this.results
            );
        }
    }
}