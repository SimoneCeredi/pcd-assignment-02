package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public interface Model {

    int getNi();

    void setNi(int ni);

    int getMaxl();

    void setMaxl(int maxl);

    int getN();

    void setN(int n);

    CompletableFuture<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> getReport(File directory);

    Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory);

    void stop();

}
