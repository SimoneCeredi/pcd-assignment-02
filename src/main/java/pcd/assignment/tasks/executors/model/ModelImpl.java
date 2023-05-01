package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounterImpl;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueueImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.tasks.executors.model.tasks.ExploreDirectoryTask;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

public class ModelImpl implements Model {
    private int ni;
    private int maxl;
    private int n;

    public ModelImpl(int ni, int maxl, int n) {
        this.ni = ni;
        this.maxl = maxl;
        this.n = n;
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
    public CompletableFuture<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> getReport(File directory) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CompletableFuture<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> ret = new CompletableFuture<>();
        ret.completeAsync(() -> {
            var res = forkJoinPool.invoke(
                    new ExploreDirectoryTask(
                            directory,
                            new IntervalLineCounterImpl(this.ni, this.maxl),
                            new LongestFilesQueueImpl(this.n)
                    )
            );
            return new Pair<>(res.getX().get(), res.getY().get());
        });
        return ret;
    }

    @Override
    public Pair<BlockingQueue<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>>, ForkJoinTask<Pair<IntervalLineCounter, LongestFilesQueue>>> analyzeSources(File directory) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        BlockingQueue<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> results = new LinkedBlockingQueue<>();
        ForkJoinTask<Pair<IntervalLineCounter, LongestFilesQueue>> future = forkJoinPool.submit(
                new ExploreDirectoryTask(
                        directory,
                        new IntervalLineCounterImpl(this.ni, this.maxl),
                        new LongestFilesQueueImpl(this.n),
                        results)
        );
        return new Pair<>(results, future);
    }

}
