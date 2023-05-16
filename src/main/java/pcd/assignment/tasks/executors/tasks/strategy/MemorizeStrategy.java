package pcd.assignment.tasks.executors.tasks.strategy;

import pcd.assignment.common.model.data.functions.Intervals;
import pcd.assignment.common.model.data.functions.LongestFiles;

public interface MemorizeStrategy {
    void saveResult(Intervals lineCounter, LongestFiles longestFiles);

    Intervals getChildLineCounter(Intervals lineCounter);

    LongestFiles getChildLongestFiles(LongestFiles filesQueue);
}
