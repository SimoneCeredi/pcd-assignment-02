package pcd.assignment.virtual.threads.source.analyzer;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.ConcurrentIntervals;
import pcd.assignment.tasks.executors.data.Intervals;
import pcd.assignment.tasks.executors.data.BaseIntervals;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.LongestFiles;
import pcd.assignment.tasks.executors.data.monitor.LongestFilesImpl;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.virtual.threads.model.tasks.factory.ExploreDirectoryTaskFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceAnalyzerImpl implements SourceAnalyzer {

    private final ExploreDirectoryTaskFactory factory = new ExploreDirectoryTaskFactory();
    private final BlockingQueue<Thread> threadList = new LinkedBlockingQueue<>();
    private final Model model;

    public SourceAnalyzerImpl(Model model) {
        this.model = model;
    }

    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory) {
        BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results = new LinkedBlockingQueue<>();
        CompletableFuture<Pair<Intervals, LongestFiles>> future = new CompletableFuture<>();
        CompletableFuture<Void> ret = new CompletableFuture<>();
        this.threadList.add(Thread.ofVirtual().start(
                this.factory.analyzeSourcesTask(
                        directory,
                        new ConcurrentIntervals(this.model.getNi(), this.model.getMaxl()),
                        new LongestFilesImpl(this.model.getN()),
                        future,
                        this.threadList,
                        results
                )
        ));
        ret.completeAsync(() -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        return new Pair<>(results, ret);
    }

    @Override
    public void stop() {
        while (!this.threadList.isEmpty()) {
            try {
                Thread t = this.threadList.take();
                t.interrupt();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
