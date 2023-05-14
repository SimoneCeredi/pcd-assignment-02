package pcd.assignment.common.model;

import pcd.assignment.common.model.data.Result;
import pcd.assignment.common.model.data.ResultsData;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;

import java.io.File;
import java.util.concurrent.CompletableFuture;


public class ModelImpl implements Model {

    private final Configuration configuration;
    private SourceAnalyzer sourceAnalyzer;
    private ResultsData resultsData;

    protected ModelImpl(Configuration configuration) {
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
    public CompletableFuture<Result> getReport(File directory) {
        return this.sourceAnalyzer.getReport(directory);
    }

    @Override
    public ResultsData analyzeSources(File directory) {
        this.resultsData = this.sourceAnalyzer.analyzeSources(directory);
        return this.resultsData;
    }

    @Override
    public int getNumberOfIntervals() {
        return configuration.getNumberOfIntervals();
    }

    @Override
    public void setNumberOfIntervals(int ni) {
        configuration.setNumberOfIntervals(ni);
    }

    @Override
    public int getMaximumLines() {
        return configuration.getMaximumLines();
    }

    @Override
    public void setMaximumLines(int maximumLines) {
        configuration.setMaximumLines(maximumLines);
    }

    @Override
    public int getAtMostNFiles() {
        return configuration.getAtMostNFiles();
    }

    @Override
    public void setAtMostNFiles(int maximumNFiles) {
        configuration.setAtMostNFiles(maximumNFiles);
    }
}
