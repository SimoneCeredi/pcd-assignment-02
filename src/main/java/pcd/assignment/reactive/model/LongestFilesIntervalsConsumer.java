package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.functions.Consumer;
import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;


public class LongestFilesIntervalsConsumer implements Consumer<FileInfo> {

    private final Intervals intervals;
    private final LongestFiles longestFiles;
    private int numFiles = 0;

    public LongestFilesIntervalsConsumer(Intervals intervals, LongestFiles longestFiles) {
        this.intervals = intervals;
        this.longestFiles = longestFiles;
    }

    @Override
    public void accept(FileInfo fileInfo) throws Throwable {
        // Here I reactively compute the maximum file in length and intervals
        // All msg must be received on the same thread
        // log("I should update maxfile and interval");
        numFiles++;
        SimpleRx.log(String.valueOf(numFiles));
        this.intervals.store(fileInfo);
        this.longestFiles.put(fileInfo);

    }



}
