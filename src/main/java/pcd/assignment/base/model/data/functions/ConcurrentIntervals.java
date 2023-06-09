package pcd.assignment.base.model.data.functions;

import pcd.assignment.base.utils.Pair;

import java.util.Collections;
import java.util.Map;

/**
 * Concurrent version of BaseIntervals
 */
public class ConcurrentIntervals extends BaseIntervals {

    public ConcurrentIntervals(int intervals, int maxLines, Map<Pair<Integer, Integer>, Counter> map) {
        super(intervals, maxLines, map);
    }

    public ConcurrentIntervals(int intervals, int maxLines) {
        super(intervals, maxLines);
    }

    @Override
    public synchronized Map<Pair<Integer, Integer>, UnmodifiableCounter> get() {
        return Collections.unmodifiableMap(this.map);
    }

    @Override
    public synchronized Intervals getCopy() {
        return new ConcurrentIntervals(this.intervals, this.maxLines, this.map);
    }

}
