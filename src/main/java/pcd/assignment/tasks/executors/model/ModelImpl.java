package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounterImpl;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueueImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.tasks.executors.model.tasks.factory.ExploreDirectoryTaskFactory;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.*;

public class ModelImpl implements Model {
    private final ExploreDirectoryTaskFactory factory = new ExploreDirectoryTaskFactory();
    private ForkJoinPool analyzeSourcesPool;
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
    public CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> getReport(File directory) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CompletableFuture<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> ret = new CompletableFuture<>();
        ret.completeAsync(() -> {
            Pair<IntervalLineCounter, LongestFilesQueue> i = forkJoinPool.invoke(
                    factory.getReportTask(
                            directory,
                            new IntervalLineCounterImpl(this.ni, this.maxl),
                            new LongestFilesQueueImpl(this.n)
                    )
            );
            return new Pair<>(i.getX(), i.getY());
        });
        return ret;
    }

    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>>, ForkJoinTask<Pair<IntervalLineCounter, LongestFilesQueue>>> analyzeSources(File directory) {
        BlockingQueue<Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue>> results = new LinkedBlockingQueue<>();
        this.analyzeSourcesPool = new ForkJoinPool();
        ForkJoinTask<Pair<IntervalLineCounter, LongestFilesQueue>> future = this.analyzeSourcesPool.submit(
                factory.analyzeSourcesTask(
                        directory,
                        new IntervalLineCounterImpl(this.ni, this.maxl),
                        new LongestFilesQueueImpl(this.n),
                        results)
        );
        return new Pair<>(results, future);
    }

    @Override
    public void stop() {
        if (this.analyzeSourcesPool != null && !this.analyzeSourcesPool.isTerminated()) {
            this.analyzeSourcesPool.shutdownNow();
        }
    }

}
