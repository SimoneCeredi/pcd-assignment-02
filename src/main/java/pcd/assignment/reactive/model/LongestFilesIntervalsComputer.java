package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.functions.Consumer;
import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;


public class LongestFilesIntervalsComputer implements Consumer<FileInfo> {

    private Intervals intervals;
    private LongestFiles longestFiles;

    public LongestFilesIntervalsComputer(Intervals intervals, LongestFiles longestFiles) {
        this.intervals = intervals;
        this.longestFiles = longestFiles;
    }

    @Override
    public void accept(FileInfo fileInfo) throws Throwable {
        this.intervals.store(fileInfo);
        this.longestFiles.put(fileInfo);
    }

}
