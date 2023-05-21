package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.functions.Consumer;
import pcd.assignment.common.model.data.functions.Intervals;
import pcd.assignment.common.model.data.functions.LongestFiles;
import pcd.assignment.common.model.data.results.FileInfo;
import pcd.assignment.common.model.data.results.Result;
import pcd.assignment.common.model.data.results.ResultImpl;

import java.util.concurrent.BlockingQueue;

/**
 * This class inserts Result(s) inside the BlockingQueue passed into the constructor.
 */
public class FunctionsConsumer implements Consumer<FileInfo> {

    private final Intervals intervals;
    private final LongestFiles longestFiles;
    private final BlockingQueue<Result> results;

    public FunctionsConsumer(Intervals intervals,
                             LongestFiles longestFiles,
                             BlockingQueue<Result> results) {
        this.intervals = intervals;
        this.longestFiles = longestFiles;
        this.results = results;
    }

    @Override
    public void accept(FileInfo fileInfo) throws Throwable {
        this.intervals.store(fileInfo);
        this.longestFiles.put(fileInfo);
        results.put(new ResultImpl(this.intervals.getCopy(), this.longestFiles.getCopy()));
    }

}
