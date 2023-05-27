package pcd.assignment.base.model.data.functions;

import pcd.assignment.base.model.data.results.FileInfo;
import pcd.assignment.base.utilities.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class implementation of Intervals
 */
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
            if (fileInfo.getNumberOfLines() >= entry.getKey().getX() && fileInfo.getNumberOfLines() <= entry.getKey().getY()) {
                entry.getValue().inc();
                break;
            }
        }

    }

    @Override
    public int getIntervals() {
        return intervals;
    }

}
