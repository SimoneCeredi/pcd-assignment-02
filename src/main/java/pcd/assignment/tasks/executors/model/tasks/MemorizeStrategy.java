package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.utilities.Pair;

public interface MemorizeStrategy {
    void saveResult(IntervalLineCounter lineCounter, LongestFilesQueue longestFiles);

    void storeSubResult(IntervalLineCounter lineCounter, LongestFilesQueue longestFiles, Pair<IntervalLineCounter, LongestFilesQueue> values);

    IntervalLineCounter getChildLineCounter(IntervalLineCounter lineCounter);

    LongestFilesQueue getChildLongestFiles(LongestFilesQueue filesQueue);
}
