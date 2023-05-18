package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import pcd.assignment.common.model.data.results.FileInfo;
import pcd.assignment.common.model.data.results.ResultImpl;
import pcd.assignment.common.analyzer.SourceAnalyzerData;

import java.io.File;

public class LineCounterVerticle extends AbstractVerticle {

    private final String file;
    private final Promise<Void> promise;
    private final SourceAnalyzerData data;

    public LineCounterVerticle(String file, Promise<Void> promise, SourceAnalyzerData data) {
        this.file = file;
        this.promise = promise;
        this.data = data;
    }

    @Override
    public void start() {
        vertx.fileSystem().readFile(this.file, res -> {
            if (res.succeeded()) {
                if (!this.data.getResultsData().isStopped()) {
                    FileInfo fileInfo = new FileInfo(new File(this.file),
                            res.result().toString().split("\\r?\\n").length);
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
        this.data.getCurrentIntervals().store(fileInfo);
        this.data.getCurrentLongestFiles().put(fileInfo);
        try {
            this.data.getResultsData().getResults().put(
                    new ResultImpl(
                            this.data.getCurrentIntervals().getCopy(),
                            this.data.getCurrentLongestFiles().getCopy()
                    )
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
