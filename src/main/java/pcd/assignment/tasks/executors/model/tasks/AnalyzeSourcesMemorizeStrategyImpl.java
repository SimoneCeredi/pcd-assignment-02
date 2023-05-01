package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class AnalyzeSourcesMemorizeStrategyImpl implements MemorizeStrategy {
    private final BlockingQueue<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> results;

    public AnalyzeSourcesMemorizeStrategyImpl(BlockingQueue<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> results) {
        this.results = results;
    }

    @Override
    public void saveResult(IntervalLineCounter lineCounter, LongestFilesQueue longestFiles) {
        try {
            this.results.put(new Pair<>(lineCounter.getCopy().get(), longestFiles.getCopy().get()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void storeSubResult(IntervalLineCounter lineCounter, LongestFilesQueue longestFiles, Pair<IntervalLineCounter, LongestFilesQueue> values) {

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
