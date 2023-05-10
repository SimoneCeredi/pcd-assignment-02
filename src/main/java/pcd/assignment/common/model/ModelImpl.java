package pcd.assignment.common.model;

import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;


public class ModelImpl implements Model {
    private final BaseModel baseModel;
    private SourceAnalyzer sourceAnalyzer;

    protected ModelImpl(int ni, int maxl, int n) {
        this.baseModel = new BaseModelImpl(ni, maxl, n);
    }

    @Override
    public void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer) {
        this.sourceAnalyzer = sourceAnalyzer;
    }

    @Override
    public int getNi() {
        return this.baseModel.getNi();
    }

    @Override
    public void setNi(int ni) {
        this.baseModel.setNi(ni);
    }

    @Override
    public int getMaxl() {
        return this.baseModel.getMaxl();
    }

    @Override
    public void setMaxl(int maxl) {
        this.baseModel.setMaxl(maxl);
    }

    @Override
    public int getN() {
        return this.baseModel.getN();
    }

    @Override
    public void setN(int n) {
        this.baseModel.setN(n);
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
