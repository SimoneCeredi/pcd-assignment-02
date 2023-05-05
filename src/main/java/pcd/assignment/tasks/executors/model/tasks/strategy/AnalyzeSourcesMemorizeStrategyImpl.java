package pcd.assignment.tasks.executors.model.tasks.strategy;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.utilities.Pair;

import java.util.concurrent.BlockingQueue;

public class AnalyzeSourcesMemorizeStrategyImpl implements MemorizeStrategy {
    private final BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> results;

    public AnalyzeSourcesMemorizeStrategyImpl(BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> results) {
        this.results = results;
    }

    @Override
    public void saveResult(IntervalLineCounter lineCounter, LongestFilesQueue longestFiles) {
        try {
            this.results.put(new Pair<>(lineCounter.getCopy(), longestFiles.getCopy()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public IntervalLineCounter getChildLineCounter(IntervalLineCounter lineCounter) {
        return lineCounter;
    }

    @Override
    public LongestFilesQueue getChildLongestFiles(LongestFilesQueue filesQueue) {
        return filesQueue;
    }
}
