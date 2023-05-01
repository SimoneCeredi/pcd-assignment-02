package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;

public interface Model {

    int getNi();

    void setNi(int ni);

    int getMaxl();

    void setMaxl(int maxl);

    int getN();

    void setN(int n);

    CompletableFuture<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> getReport(File directory);

    Pair<BlockingQueue<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>>, ForkJoinTask<Pair<IntervalLineCounter, LongestFilesQueue>>> analyzeSources(File directory);

}
