package pcd.assignment.tasks.executors.tasks.strategy;

import pcd.assignment.common.model.data.results.Result;
import pcd.assignment.common.model.data.results.ResultImpl;
import pcd.assignment.common.model.data.functions.Intervals;
import pcd.assignment.common.model.data.functions.LongestFiles;

import java.util.concurrent.BlockingQueue;

public class AnalyzeSourcesMemorizeStrategyImpl implements MemorizeStrategy {
    private final BlockingQueue<Result> results;

    public AnalyzeSourcesMemorizeStrategyImpl(BlockingQueue<Result> results) {
        this.results = results;
    }

    @Override
    public void saveResult(Intervals lineCounter, LongestFiles longestFiles) {
        try {
            this.results.put(new ResultImpl(lineCounter.getCopy(), longestFiles.getCopy()));
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
