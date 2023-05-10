package pcd.assignment.common.source.analyzer;

import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public interface SourceAnalyzer {
    default CompletableFuture<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> getReport(File directory) {
        var res = this.analyzeSources(directory);
        CompletableFuture<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> ret = new CompletableFuture<>();
        res.getY().whenComplete((unused, throwable) -> ret.completeAsync(() -> {
            List<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> coll = new ArrayList<>();
            res.getX().drainTo(coll);
            var last = coll.get(coll.size() - 1);
            return new Pair<>(last.getX(), last.getY());
        }));
        return ret;
    }

    Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory);

    void stop();

}
