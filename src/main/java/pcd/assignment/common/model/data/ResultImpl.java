package pcd.assignment.common.model.data;

import pcd.assignment.common.model.data.monitor.LongestFiles;

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
