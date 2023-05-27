package pcd.assignment.base.analyzer;

import pcd.assignment.base.model.data.functions.Intervals;
import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.base.model.data.functions.LongestFiles;

/**
 * SourceAnalyzerData interface
 */
public interface SourceAnalyzerData {

    ResultsData getResultsData();

    Intervals getCurrentIntervals();

    LongestFiles getCurrentLongestFiles();

}
