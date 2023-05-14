package pcd.assignment.common.model;

import pcd.assignment.common.model.data.ResultsData;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;

public interface Model extends SourceAnalyzer {

    void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer);

    ResultsData getResultsData();

    Configuration getConfiguration();

}
