package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.Pair;

import java.io.File;

public class LineCounterVerticle extends AbstractVerticle {

    private final String file;
    private final Promise<Void> promise;
    private final SourceAnalyzerData model;

    public LineCounterVerticle(String file, Promise<Void> promise, SourceAnalyzerData model) {
        this.file = file;
        this.promise = promise;
        this.model = model;
    }

    @Override
    public void start() {
        vertx.fileSystem().readFile(this.file, res -> {
            if (res.succeeded()) {
                FileInfo fileInfo = new FileInfo(new File(this.file), res.result().toString().split("\\r?\\n").length);
                if (!this.model.shouldStop()) {
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
        this.model.getIntervals().store(fileInfo);
        this.model.getLongestFiles().put(fileInfo);
        try {
            this.model.getResults().put(new Pair<>(this.model.getIntervals().getCopy(), this.model.getLongestFiles().getCopy()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
