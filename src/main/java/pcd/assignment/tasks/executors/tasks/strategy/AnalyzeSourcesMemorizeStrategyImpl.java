package pcd.assignment.tasks.executors.tasks.strategy;

import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.Intervals;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;

import java.util.concurrent.BlockingQueue;

public class AnalyzeSourcesMemorizeStrategyImpl implements MemorizeStrategy {
    private final BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results;

    public AnalyzeSourcesMemorizeStrategyImpl(BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results) {
        this.results = results;
    }

    @Override
    public void saveResult(Intervals lineCounter, LongestFiles longestFiles) {
        try {
            this.results.put(new Pair<>(lineCounter.getCopy(), longestFiles.getCopy()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Intervals getChildLineCounter(Intervals lineCounter) {
        return lineCounter;
    }

    @Override
    public LongestFiles getChildLongestFiles(LongestFiles filesQueue) {
        return filesQueue;
    }
}
