package pcd.assignment.impl.reactive.model.data;

import pcd.assignment.base.model.data.functions.BaseIntervals;
import pcd.assignment.base.model.data.functions.Intervals;
import pcd.assignment.base.model.data.functions.Counter;
import pcd.assignment.base.model.data.functions.UnmodifiableCounter;
import pcd.assignment.base.utilities.Pair;

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
