package pcd.assignment.tasks.executors.model.data;

import pcd.assignment.tasks.executors.model.data.monitor.Counter;
import pcd.assignment.tasks.executors.model.data.monitor.CounterImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IntervalLineCounterImpl implements IntervalLineCounter {
    private final HashMap<Pair<Integer, Integer>, Counter> map;

    public IntervalLineCounterImpl(int intervals, int maxLines) {
        this.map = new HashMap<>();
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
    public Map<Pair<Integer, Integer>, UnmodifiableCounter> get() {
        return Collections.unmodifiableMap(new HashMap<>(this.map));
    }
}
