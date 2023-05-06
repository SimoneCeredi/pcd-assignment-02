package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.FileProps;

import java.util.List;


public class DirectoryExplorerVerticle extends AbstractVerticle {


    @Override
    public void start() {
        vertx.eventBus().<String>consumer("explore-directory", message -> {
            String directory = message.body();
            vertx.fileSystem().readDir(directory, res -> {
                if (res.succeeded()) {
                    exploreDirectory(res.result());
                } else {
                    System.err.println("Failed to read directory: " + res.cause().getMessage());
                }
            });
        });
    }

    private void exploreDirectory(List<String> fileList) {
        for (String file : fileList) {
            vertx.fileSystem().props(file, res -> {
                if (res.succeeded()) {
                    FileProps fileProps = res.result();
                    if (fileProps.isDirectory()) {
                        vertx.eventBus().send("explore-directory", file);
                    } else {
                        if (file.endsWith(".java")) {
                            vertx.eventBus().send("process-file", file);
                        }
                    }
                } else {
                    System.err.println("Failed to get file properties: " + res.cause().getMessage());
                }
            });
        }
    }

    
}
