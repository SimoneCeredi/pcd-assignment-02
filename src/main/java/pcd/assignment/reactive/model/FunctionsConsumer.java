package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.functions.Consumer;
import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.utilities.Pair;

import java.util.concurrent.BlockingQueue;


public class FunctionsConsumer implements Consumer<FileInfo> {

    private final Intervals intervals;
    private final LongestFiles longestFiles;
    private final BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results;

    public FunctionsConsumer(Intervals intervals,
                             LongestFiles longestFiles,
                             BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results) {
        this.intervals = intervals;
        this.longestFiles = longestFiles;
        this.results = results;
    }

    @Override
    public void accept(FileInfo fileInfo) throws Throwable {
        // Here I reactively compute the maximum file in length and intervals
        // All msg must be received on the same thread
        // log("I should update maxfile and interval");
        this.intervals.store(fileInfo);
        this.longestFiles.put(fileInfo);
        results.put(new Pair<>(this.intervals, this.longestFiles));
    }

}
