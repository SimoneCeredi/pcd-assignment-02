package pcd.assignment.common.source.analyzer;

import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.utilities.Pair;

import java.util.concurrent.BlockingQueue;

public interface SourceAnalyzerData {
    BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> getResults();

    Intervals getIntervals();

    LongestFiles getLongestFiles();

    boolean shouldStop();
}
