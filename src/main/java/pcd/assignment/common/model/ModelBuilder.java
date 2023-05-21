package pcd.assignment.common.model;

import pcd.assignment.common.model.configuration.Configuration;
import pcd.assignment.common.analyzer.SourceAnalyzer;

import java.util.function.Function;

/**
 * Model builder interface
 */
public interface ModelBuilder {

    ModelBuilder setConfiguration(Configuration configuration);

    ModelBuilder setSourceAnalyzer(Function<Model, SourceAnalyzer> sourceAnalyzer);

    Model build();

}
