package pcd.assignment.common.model;

import pcd.assignment.common.model.configuration.Configuration;
import pcd.assignment.common.model.data.results.ResultsData;
import pcd.assignment.common.analyzer.SourceAnalyzer;

public interface Model extends SourceAnalyzer {

    void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer);

    ResultsData getResultsData();

    Configuration getConfiguration();

}
