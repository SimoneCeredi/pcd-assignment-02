package pcd.assignment.tasks.executors.tasks.strategy;

import pcd.assignment.tasks.executors.data.Intervals;
import pcd.assignment.tasks.executors.data.monitor.LongestFiles;

public interface MemorizeStrategy {
    void saveResult(Intervals lineCounter, LongestFiles longestFiles);

    Intervals getChildLineCounter(Intervals lineCounter);

    LongestFiles getChildLongestFiles(LongestFiles filesQueue);
}
