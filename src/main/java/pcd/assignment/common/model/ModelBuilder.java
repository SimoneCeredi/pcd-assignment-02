package pcd.assignment.common.model;

import pcd.assignment.common.source.analyzer.SourceAnalyzer;

import java.util.function.Function;

public interface ModelBuilder {
    ModelBuilder setNi(int ni);

    ModelBuilder setMaxl(int maxl);

    ModelBuilder setN(int n);

    ModelBuilder setSourceAnalyzer(Function<Model, SourceAnalyzer> sourceAnalyzer);

    Model build();
}
