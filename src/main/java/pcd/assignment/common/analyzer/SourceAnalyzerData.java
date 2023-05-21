package pcd.assignment.common.analyzer;

import pcd.assignment.common.model.data.functions.Intervals;
import pcd.assignment.common.model.data.results.ResultsData;
import pcd.assignment.common.model.data.functions.LongestFiles;

/**
 * SourceAnalyzerData interface
 */
public interface SourceAnalyzerData {

    ResultsData getResultsData();

    Intervals getCurrentIntervals();

    LongestFiles getCurrentLongestFiles();

}
