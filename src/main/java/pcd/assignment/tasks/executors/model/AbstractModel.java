package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractModel implements Model {
    private int ni;
    private int maxl;
    private int n;

    public AbstractModel(int ni, int maxl, int n) {
        this.ni = ni;
        this.maxl = maxl;
        this.n = n;
    }

    @Override
    public CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> getReport(File directory) {
        var res = this.analyzeSources(directory);
        CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> ret = new CompletableFuture<>();
        res.getY().whenComplete((unused, throwable) -> ret.completeAsync(() -> {
            List<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> coll = new ArrayList<>();
            res.getX().drainTo(coll);
            var last = coll.get(coll.size() - 1);
            return new Pair<>(last.getX(), last.getY());
        }));
        return ret;
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
}
