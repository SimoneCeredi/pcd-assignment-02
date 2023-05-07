package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import io.vertx.core.file.FileProps;
import pcd.assignment.event.loop.model.ModelData;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class DirectoryExplorerVerticle extends AbstractVerticle {

    private final String directory;
    private final Promise<Void> promise;
    private final BlockingQueue<String> deployedVerticles;
    private final List<Promise<Void>> promises = new LinkedList<>();
    private final ModelData model;

    public DirectoryExplorerVerticle(String directory, Promise<Void> promise, BlockingQueue<String> deployedVerticles, ModelData model) {
        this.directory = directory;
        this.promise = promise;
        this.deployedVerticles = deployedVerticles;
        this.model = model;
    }

    @Override
    public void start() {
        vertx.fileSystem().readDir(this.directory, res -> {
            if (res.succeeded()) {
                exploreDirectory(res.result());
            } else {
                System.err.println("Failed to read directory: " + res.cause().getMessage());
                this.promise.fail(res.cause().getMessage());
            }
        });

    }

    private void completePromise() {
        CompositeFuture.all(this.promises.stream().map(Promise::future).collect(Collectors.toList())).onComplete(as -> {
            if (as.succeeded()) {
                this.promise.complete();
                System.out.println(directory + " Completed");
            } else {
                System.err.println("Error exploring directory " + as.cause().getMessage());
                this.promise.fail(as.cause().getMessage());
            }
        });
    }

    private void exploreDirectory(List<String> fileList) {
        AtomicInteger i = new AtomicInteger();
        for (String file : fileList) {
            Promise<Void> explorePromise = Promise.promise();
            this.promises.add(explorePromise);
            vertx.fileSystem().props(file, res -> {
                if (res.succeeded()) {
                    FileProps fileProps = res.result();
                    if (fileProps.isDirectory()) {
                        vertx.deployVerticle(new DirectoryExplorerVerticle(file, this.promises.get(i.get()), deployedVerticles, model));
                    } else {
                        if (file.endsWith(".java")) {
                            vertx.deployVerticle(new LineCounterVerticle(file, this.promises.get(i.get()), model));
                        }
                    }
                    i.getAndIncrement();
                } else {
                    System.err.println("Failed to get file properties: " + res.cause().getMessage());
                }
            });
        }
        completePromise();
    }


}
