package pcd.assignment.base.model;

import pcd.assignment.base.model.configuration.Configuration;
import pcd.assignment.base.model.data.results.Result;
import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.base.analyzer.SourceAnalyzer;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * Model implementation.
 */
public class ModelImpl implements Model {

    private final Configuration configuration;
    private SourceAnalyzer sourceAnalyzer;
    private ResultsData resultsData;

    public ModelImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer) {
        this.sourceAnalyzer = sourceAnalyzer;
    }

    @Override
    public ResultsData getResultsData() {
        return this.resultsData;
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public CompletableFuture<Result> getReport(File directory) {
        return this.sourceAnalyzer.getReport(directory);
    }

    @Override
    public ResultsData analyzeSources(File directory) {
        this.resultsData = this.sourceAnalyzer.analyzeSources(directory);
        return this.resultsData;
    }


}
