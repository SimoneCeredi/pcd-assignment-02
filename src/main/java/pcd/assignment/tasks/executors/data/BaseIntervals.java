package pcd.assignment.tasks.executors.data;

import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.monitor.Counter;
import pcd.assignment.tasks.executors.data.monitor.CounterImpl;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableCounter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseIntervals implements Intervals {
    protected final Map<Pair<Integer, Integer>, Counter> map;

    protected final int intervals;

    protected final int maxLines;

    public BaseIntervals(int intervals, int maxLines, Map<Pair<Integer, Integer>, Counter> map) {
        Map<Pair<Integer, Integer>, Counter> mapCopy = new HashMap<>();
        map.forEach((key, value) -> mapCopy.put(key, new CounterImpl(value.getValue())));
        this.map = mapCopy;
        this.intervals = intervals;
        this.maxLines = maxLines;

    }

    public BaseIntervals(int intervals, int maxLines) {
        this.map = new HashMap<>();
        this.intervals = intervals;
        this.maxLines = maxLines;

        intervals = intervals - 1;
        int intervalSize = maxLines / intervals;
        int lowerBound = 0;
        int upperBound = intervalSize - 1;

        for (int i = 0; i < intervals; i++) {
            this.map.put(new Pair<>(lowerBound, upperBound), new CounterImpl());
            lowerBound = upperBound + 1;
            upperBound = upperBound + intervalSize;
        }
        this.map.put(new Pair<>(maxLines, Integer.MAX_VALUE), new CounterImpl());

    }

    @Override
    public void store(FileInfo fileInfo) {
        for (Map.Entry<Pair<Integer, Integer>, Counter> entry : this.map.entrySet()) {
            if (fileInfo.getLineCount() >= entry.getKey().getX() && fileInfo.getLineCount() <= entry.getKey().getY()) {
                entry.getValue().inc();
                break;
            }
        }

    }

    @Override
    public void storeAll(Intervals lineCounter) {
        lineCounter.get().forEach((key, value) ->
                this.map.put(key, new CounterImpl(value.getValue() + this.map.get(key).getValue()))
        );
    }

    @Override
    public int getIntervals() {
        return intervals;
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

}
