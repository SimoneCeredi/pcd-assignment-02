package pcd.assignment.common.model;

import pcd.assignment.common.model.configuration.Configuration;
import pcd.assignment.common.analyzer.SourceAnalyzer;

import java.util.function.Function;

/**
 * A ModelBuilder implementation
 */
public class ModelBuilderImpl implements ModelBuilder {
    private Model model;
    private SourceAnalyzer sourceAnalyzer;
    private Configuration configuration;


    @Override
    public ModelBuilder setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public ModelBuilder setSourceAnalyzer(Function<Model, SourceAnalyzer> sourceAnalyzer) {
        this.model = new ModelImpl(configuration);
        this.sourceAnalyzer = sourceAnalyzer.apply(this.model);
        return this;
    }

    @Override
    public Model build() {
        if (this.sourceAnalyzer == null) {
            throw new IllegalStateException("Not all required parameters have been set.");
        }
        this.model.setSourceAnalyzer(this.sourceAnalyzer);
        return this.model;
    }
}
