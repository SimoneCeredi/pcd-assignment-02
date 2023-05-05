package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
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

    CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> getReport(File directory);

    Pair<BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>>, CompletableFuture<Void>> analyzeSources(File directory);

    void stop();

}
