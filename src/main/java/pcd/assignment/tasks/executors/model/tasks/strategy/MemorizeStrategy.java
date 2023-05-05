package pcd.assignment.tasks.executors.model.tasks.strategy;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;

public interface MemorizeStrategy {
    void saveResult(IntervalLineCounter lineCounter, LongestFilesQueue longestFiles);
    
    IntervalLineCounter getChildLineCounter(IntervalLineCounter lineCounter);

    LongestFilesQueue getChildLongestFiles(LongestFilesQueue filesQueue);
}
