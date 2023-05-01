package pcd.assignment.tasks.executors.model.tasks.strategy;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounterImpl;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueueImpl;
import pcd.assignment.utilities.Pair;

public class GetReportMemorizeStrategyImpl implements MemorizeStrategy {
    @Override
    public void saveResult(IntervalLineCounter lineCounter, LongestFilesQueue longestFiles) {

    }

    @Override
    public void storeSubResult(IntervalLineCounter lineCounter, LongestFilesQueue longestFiles, Pair<IntervalLineCounter, LongestFilesQueue> values) {
        lineCounter.storeAll(values.getX());
        longestFiles.putAll(values.getY());
    }


    @Override
    public IntervalLineCounter getChildLineCounter(IntervalLineCounter lineCounter) {
        return new IntervalLineCounterImpl(lineCounter.getIntervals(), lineCounter.getMaxLines());
    }

    @Override
    public LongestFilesQueue getChildLongestFiles(LongestFilesQueue filesQueue) {
        return new LongestFilesQueueImpl(filesQueue.getFilesToKeep());
    }
}
