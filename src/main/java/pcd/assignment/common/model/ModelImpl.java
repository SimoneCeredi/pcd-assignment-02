package pcd.assignment.common.model;

import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

// TODO: will become the only model impl.
// TODO: A builder used to create the impl to specify which Source analyzer to setUp
public class ModelImpl implements Model {
    private SourceAnalyzer sourceAnalyzer;
    private int ni;
    private int maxl;
    private int n;

    protected ModelImpl(int ni, int maxl, int n) {
        this.ni = ni;
        this.maxl = maxl;
        this.n = n;
    }

    @Override
    public void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer) {
        this.sourceAnalyzer = sourceAnalyzer;
    }

    @Override
    public int getNi() {
        return ni;
    }

    @Override
    public void setNi(int ni) {
        this.ni = ni;
    }

    @Override
    public int getMaxl() {
        return maxl;
    }

    @Override
    public void setMaxl(int maxl) {
        this.maxl = maxl;
    }

    @Override
    public int getN() {
        return n;
    }

    @Override
    public void setN(int n) {
        this.n = n;
    }

    @Override
    public CompletableFuture<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> getReport(File directory) {
        return this.sourceAnalyzer.getReport(directory);
    }

    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory) {
        return this.sourceAnalyzer.analyzeSources(directory);
    }

    @Override
    public void stop() {
        this.sourceAnalyzer.stop();
    }
}
