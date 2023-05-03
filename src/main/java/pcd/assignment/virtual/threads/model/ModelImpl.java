package pcd.assignment.virtual.threads.model;

import pcd.assignment.model.AbstractModel;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;

public class ModelImpl extends AbstractModel {
    public ModelImpl(int ni, int maxl, int n) {
        super(ni, maxl, n);
    }

    @Override
    public CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> getReport(File directory) {
        return null;
    }

    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>>, ForkJoinTask<Pair<IntervalLineCounter, LongestFilesQueue>>> analyzeSources(File directory) {
        return null;
    }

    @Override
    public void stop() {

    }
}
