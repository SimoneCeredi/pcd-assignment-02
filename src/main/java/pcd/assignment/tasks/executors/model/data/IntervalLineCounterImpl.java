package pcd.assignment.tasks.executors.model.data;

import pcd.assignment.tasks.executors.model.data.monitor.Counter;
import pcd.assignment.tasks.executors.model.data.monitor.CounterImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IntervalLineCounterImpl implements IntervalLineCounter {
    private final Map<Pair<Integer, Integer>, Counter> map;

    private final int intervals;

    private final int maxLines;

    public IntervalLineCounterImpl(int intervals, int maxLines, Map<Pair<Integer, Integer>, Counter> map) {
        Map<Pair<Integer, Integer>, Counter> mapCopy = new HashMap<>();
        map.forEach((key, value) -> mapCopy.put(key, new CounterImpl(value.getValue())));
        this.map = mapCopy;
        this.intervals = intervals;
        this.maxLines = maxLines;

    }

    public IntervalLineCounterImpl(int intervals, int maxLines) {
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
    public void storeAll(IntervalLineCounter lineCounter) {
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

    @Override
    public Map<Pair<Integer, Integer>, UnmodifiableCounter> get() {
        return Collections.unmodifiableMap(this.map);
    }

    @Override
    public IntervalLineCounter getCopy() {
        return new IntervalLineCounterImpl(this.intervals, this.maxLines, this.map);
    }
}
