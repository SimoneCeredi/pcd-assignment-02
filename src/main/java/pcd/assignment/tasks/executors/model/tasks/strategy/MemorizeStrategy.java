package pcd.assignment.tasks.executors.model.tasks.strategy;

import pcd.assignment.tasks.executors.model.data.Intervals;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFiles;

public interface MemorizeStrategy {
    void saveResult(Intervals lineCounter, LongestFiles longestFiles);

    Intervals getChildLineCounter(Intervals lineCounter);

    LongestFiles getChildLongestFiles(LongestFiles filesQueue);
}
