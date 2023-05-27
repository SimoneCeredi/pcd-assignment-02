package pcd.assignment.base.model;

import pcd.assignment.base.model.configuration.Configuration;
import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.base.analyzer.SourceAnalyzer;

/**
 * Model interface.
 */
public interface Model extends SourceAnalyzer {

    void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer);

    ResultsData getResultsData();

    Configuration getConfiguration();

}
