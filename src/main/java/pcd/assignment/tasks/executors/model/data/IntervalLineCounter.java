package pcd.assignment.tasks.executors.model.data;

import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.util.Map;

public interface IntervalLineCounter {
    void store(FileInfo fileInfo);

    void storeAll(IntervalLineCounter lineCounter);

    int getIntervals();

    int getMaxLines();

    Map<Pair<Integer, Integer>, UnmodifiableCounter> get();

    IntervalLineCounter getCopy();
}
