package pcd.assignment.common.model;

import pcd.assignment.common.source.analyzer.SourceAnalyzer;

import java.util.function.Function;

public class ModelBuilderImpl implements ModelBuilder {
    private int ni;
    private int maxl;
    private int n;
    private Model model;
    private SourceAnalyzer sourceAnalyzer;


    @Override
    public ModelBuilder setNi(int ni) {
        this.ni = ni;
        return this;
    }

    @Override
    public ModelBuilder setMaxl(int maxl) {
        this.maxl = maxl;
        return this;
    }

    @Override
    public ModelBuilder setN(int n) {
        this.n = n;
        return this;
    }

    @Override
    public ModelBuilder setSourceAnalyzer(Function<Model, SourceAnalyzer> sourceAnalyzer) {
        this.model = new ModelImpl(this.ni, this.maxl, this.n);
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
