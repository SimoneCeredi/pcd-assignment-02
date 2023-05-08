package pcd.assignment.event.loop.model;

import pcd.assignment.tasks.executors.model.data.Intervals;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.utilities.Pair;

import java.util.concurrent.BlockingQueue;

public interface ModelData {
    BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> getResults();

    Intervals getIntervals();

    LongestFiles getLongestFiles();

    boolean shouldStop();
}
