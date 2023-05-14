package pcd.assignment.common.model;

import pcd.assignment.common.source.analyzer.SourceAnalyzer;

import java.util.function.Function;

public interface ModelBuilder {

    ModelBuilder setConfiguration(Configuration configuration);

    ModelBuilder setSourceAnalyzer(Function<Model, SourceAnalyzer> sourceAnalyzer);

    Model build();
}
