package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.tasks.ExploreDirectoryTask;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class ModelImpl implements Model {
    private final IntervalLineCounter intervalLineCounter;
    private final LongestFilesQueue longestFiles;
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public ModelImpl(IntervalLineCounter intervalLineCounter, LongestFilesQueue longestFiles) {
        this.intervalLineCounter = intervalLineCounter;
        this.longestFiles = longestFiles;
    }

    @Override
    public Pair<IntervalLineCounter, LongestFilesQueue> getReport(File directory) {
        return forkJoinPool.invoke(new ExploreDirectoryTask(directory, this.intervalLineCounter, this.longestFiles));
    }

    @Override
    public BlockingQueue<Pair<IntervalLineCounter, LongestFilesQueue>> analyzeSources(File directory) {
        BlockingQueue<Pair<IntervalLineCounter, LongestFilesQueue>> results = new LinkedBlockingQueue<>();
        forkJoinPool.invoke(new ExploreDirectoryTask(directory, this.intervalLineCounter, this.longestFiles, results));
        System.out.println("YOOOOO");
        return results;
    }
}
