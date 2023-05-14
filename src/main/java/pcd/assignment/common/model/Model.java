package pcd.assignment.common.model;

import pcd.assignment.common.model.data.ResultsData;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;

public interface Model extends Configuration, SourceAnalyzer {

    void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer);

    ResultsData getResultsData();

}
