package pcd.assignment.base.model;

import pcd.assignment.base.model.configuration.Configuration;
import pcd.assignment.base.analyzer.SourceAnalyzer;

import java.util.function.Function;

/**
 * Model builder interface
 */
public interface ModelBuilder {

    ModelBuilder setConfiguration(Configuration configuration);

    ModelBuilder setSourceAnalyzer(Function<Model, SourceAnalyzer> sourceAnalyzer);

    Model build();

}
