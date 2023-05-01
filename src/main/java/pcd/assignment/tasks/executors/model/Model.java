package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public interface Model {

    int getNi();

    void setNi(int ni);

    int getMaxl();

    void setMaxl(int maxl);

    int getN();

    void setN(int n);

    CompletableFuture<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> getReport(File directory);

    BlockingQueue<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> analyzeSources(File directory);

    void changeParams(int ni, int maxl, int n);


}
