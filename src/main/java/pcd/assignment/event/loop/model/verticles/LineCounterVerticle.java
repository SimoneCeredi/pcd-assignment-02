package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import pcd.assignment.event.loop.model.ModelData;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.utilities.Pair;

import java.io.File;

public class LineCounterVerticle extends AbstractVerticle {

    private final String file;
    private final Promise<Void> promise;
    private final ModelData model;

    public LineCounterVerticle(String file, Promise<Void> promise, ModelData model) {
        this.file = file;
        this.promise = promise;
        this.model = model;
    }

    @Override
    public void start() {
        vertx.fileSystem().readFile(this.file, res -> {
            if (res.succeeded()) {
                FileInfo fileInfo = new FileInfo(new File(this.file), res.result().toString().split("\\r?\\n").length);
                this.model.getIntervals().store(fileInfo);
                this.model.getLongestFiles().put(fileInfo);
                try {
                    this.model.getResults().put(new Pair<>(this.model.getIntervals().getCopy(), this.model.getLongestFiles().getCopy()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.promise.complete();
            } else {
                System.err.println("Failed to read the file " + res.cause().getMessage());
                this.promise.fail(res.cause().getMessage());
            }
        });
    }
}
