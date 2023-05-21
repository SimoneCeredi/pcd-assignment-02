package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import io.vertx.core.file.FileProps;
import pcd.assignment.common.analyzer.SourceAnalyzerData;
import pcd.assignment.event.loop.utils.VerticleDeployUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Directory explorer implemented as an AbstractVerticle.
 */
public class DirectoryExplorerVerticle extends AbstractVerticle {

    private final String directory;
    private final Promise<Void> promise;
    private final SourceAnalyzerData data;

    public DirectoryExplorerVerticle(String directory,
                                     Promise<Void> promise,
                                     SourceAnalyzerData data) {
        this.directory = directory;
        this.promise = promise;
        this.data = data;
    }

    /**
     * Explores all the files of the current directory.
     */
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

    /**
     * Explores all the files passed as input from the start() method.
     * The promise is completed once all the children's are completed.
     * @param fileList
     */
    private void exploreDirectory(List<String> fileList) {
        List<Promise<Void>> filePromises = new ArrayList<>(fileList.size());
        for (int i = 0; i < fileList.size() &&
                !this.data.getResultsData().isStopped(); i++) {
            String file = fileList.get(i);
            Promise<Void> filePromise = Promise.promise();
            filePromises.add(filePromise);
            vertx.fileSystem().props(file, res -> {
                if (res.succeeded()) {
                    // Deploy the specific verticle based on the file's type
                    manageProps(file, filePromise, res);
                } else {
                    System.err.println("Failed to get file properties: " + res.cause().getMessage());
                    filePromise.fail(res.cause().getMessage());
                }
            });
        }
        if (this.data.getResultsData().isStopped()) {
            this.promise.complete();
        } else {
            // The composite future wraps a list of futures, it is useful when several futures need to be coordinated.
            CompositeFuture.all(filePromises.stream().map(Promise::future).collect(Collectors.toList()))
                    .onFailure(e -> this.promise.fail(e.getCause()))
                    .onComplete(as -> this.promise.complete());
        }
    }

    /**
     * If file props:
     *  - Is a directory, deploy a new DirectoryExplorerVerticle on
     *      the same Vertx instance.
     *  - Is a file, call exploreFile()
     * @param file which represents the directory name
     * @param filePromise to async wait the completion
     * @param res the FileProps
     */
    private void manageProps(String file, Promise<Void> filePromise, AsyncResult<FileProps> res) {
        FileProps fileProps = res.result();
        if (fileProps.isDirectory()) {
            vertx.deployVerticle(new DirectoryExplorerVerticle(file, filePromise, this.data),
                    VerticleDeployUtils.getDeploymentOptions());
        } else {
            exploreFile(file, filePromise);
        }
    }

    /**
     * Method called by manageProps.
     * It deploys a ReadLinesVerticle whenever the file name passed as parameter is a Java source.
     * @param file
     * @param filePromise
     */
    private void exploreFile(String file, Promise<Void> filePromise) {
        if (file.endsWith(".java")) {
            vertx.deployVerticle(new ReadLinesVerticle(file, filePromise, this.data),
                    VerticleDeployUtils.getDeploymentOptions());
        } else {
            filePromise.complete();
        }
    }

}
