package pcd.assignment.tasks.executors.tasks.strategy;

import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;

public interface MemorizeStrategy {
    void saveResult(Intervals lineCounter, LongestFiles longestFiles);

    Intervals getChildLineCounter(Intervals lineCounter);

    LongestFiles getChildLongestFiles(LongestFiles filesQueue);
}
