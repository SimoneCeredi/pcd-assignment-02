package pcd.assignment.common.source.analyzer;

import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.ResultsData;
import pcd.assignment.common.model.data.monitor.LongestFiles;

public class SourceAnalyzerDataImpl implements SourceAnalyzerData {

    private final ResultsData resultsData;
    private final Intervals currentIntervals;
    private final LongestFiles longestFiles;

    public SourceAnalyzerDataImpl(ResultsData resultsData, Intervals currentIntervals, LongestFiles longestFiles) {
        this.resultsData = resultsData;
        this.currentIntervals = currentIntervals;
        this.longestFiles = longestFiles;
    }

    @Override
    public ResultsData getResultsData() {
        return this.resultsData;
    }

    @Override
    public Intervals getCurrentIntervals() {
        return this.currentIntervals;
    }

    @Override
    public LongestFiles getCurrentLongestFiles() {
        return this.longestFiles;
    }
}