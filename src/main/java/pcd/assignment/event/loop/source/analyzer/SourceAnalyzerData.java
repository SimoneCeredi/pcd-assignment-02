package pcd.assignment.event.loop.source.analyzer;

import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.Intervals;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;

import java.util.concurrent.BlockingQueue;

public interface SourceAnalyzerData {
    BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> getResults();

    Intervals getIntervals();

    LongestFiles getLongestFiles();

    boolean shouldStop();
}
