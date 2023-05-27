package pcd.assignment.base.model.data.results;

import pcd.assignment.base.model.data.functions.Intervals;
import pcd.assignment.base.model.data.functions.LongestFiles;

/**
 * A Result implementation
 */
public class ResultImpl implements Result {

    private Intervals intervals;
    private LongestFiles longestFiles;

    public ResultImpl(Intervals intervals, LongestFiles longestFiles) {
        this.intervals = intervals;
        this.longestFiles = longestFiles;
    }

    @Override
    public Intervals getIntervals() {
        return this.intervals;
    }

    @Override
    public LongestFiles getLongestFiles() {
        return this.longestFiles;
    }
}
