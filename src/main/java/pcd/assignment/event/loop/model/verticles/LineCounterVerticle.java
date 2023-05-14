package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.source.analyzer.StoppableSourceAnalyzer;
import pcd.assignment.common.utilities.Pair;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class LineCounterVerticle extends AbstractVerticle {

    private final String file;
    private final Promise<Void> promise;
    private final Intervals intervals;
    private final LongestFiles longestFiles;
    private final BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results;
    private final StoppableSourceAnalyzer sourceAnalyzer;

    public LineCounterVerticle(String file, Promise<Void> promise, Intervals intervals, LongestFiles longestFiles, BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results, StoppableSourceAnalyzer sourceAnalyzer) {
        this.file = file;
        this.promise = promise;
        this.intervals = intervals;
        this.longestFiles = longestFiles;
        this.results = results;
        this.sourceAnalyzer = sourceAnalyzer;
    }

    @Override
    public void start() {
        vertx.fileSystem().readFile(this.file, res -> {
            if (res.succeeded()) {
                FileInfo fileInfo = new FileInfo(new File(this.file), res.result().toString().split("\\r?\\n").length);
                if (!this.sourceAnalyzer.shouldStop()) {
                    saveFileInfo(fileInfo);
                }
                this.promise.complete();
            } else {
                System.err.println("Failed to read the file " + res.cause().getMessage());
                this.promise.fail(res.cause().getMessage());
            }
        });
    }

    private void saveFileInfo(FileInfo fileInfo) {
        this.intervals.store(fileInfo);
        this.longestFiles.put(fileInfo);
        try {
            this.results.put(new Pair<>(this.intervals.getCopy(), this.longestFiles.getCopy()));
        } catch (InterruptedException ignored) {
            // This happens on stop button press
        }
    }
}
