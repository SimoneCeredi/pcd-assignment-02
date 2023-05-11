package pcd.assignment.reactive.model.data;

import pcd.assignment.common.model.data.BaseIntervals;
import pcd.assignment.common.model.data.ConcurrentIntervals;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.Counter;
import pcd.assignment.common.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.common.utilities.Pair;

import java.util.Collections;
import java.util.Map;

public class SimpleIntervals extends BaseIntervals {

    public SimpleIntervals(int intervals, int maxLines, Map<Pair<Integer, Integer>, Counter> map) {
        super(intervals, maxLines, map);
    }

    public SimpleIntervals(int intervals, int maxLines) {
        super(intervals, maxLines);
    }

    @Override
    public Map<Pair<Integer, Integer>, UnmodifiableCounter> get() {
        return Collections.unmodifiableMap(this.map);
    }

    @Override
    public Intervals getCopy() {
        return new SimpleIntervals(this.intervals, this.maxLines, this.map);
    }

}
