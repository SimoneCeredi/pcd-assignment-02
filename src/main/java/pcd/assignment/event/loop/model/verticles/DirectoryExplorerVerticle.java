package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import io.vertx.core.file.FileProps;
import pcd.assignment.common.model.data.Intervals;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.LongestFiles;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.source.analyzer.StoppableSourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.event.loop.utils.VerticleDeployUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;


public class DirectoryExplorerVerticle extends AbstractVerticle {

    private final String directory;
    private final Promise<Void> promise;
    private final StoppableSourceAnalyzer sourceAnalyzer;
    private final Intervals intervals;
    private final LongestFiles longestFiles;
    private final BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results;

    public DirectoryExplorerVerticle(String directory, Promise<Void> promise, Intervals intervals, LongestFiles longestFiles, BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results, StoppableSourceAnalyzer sourceAnalyzer) {
        this.directory = directory;
        this.promise = promise;
        this.intervals = intervals;
        this.longestFiles = longestFiles;
        this.results = results;
        this.sourceAnalyzer = sourceAnalyzer;
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


    private void exploreDirectory(List<String> fileList) {
        List<Promise<Void>> filePromises = new ArrayList<>(fileList.size());
        for (String file : fileList) {
            if (!this.sourceAnalyzer.shouldStop()) {
                Promise<Void> filePromise = Promise.promise();
                filePromises.add(filePromise);
                vertx.fileSystem().props(file, res -> {
                    if (res.succeeded()) {
                        manageProps(file, filePromise, res);
                    } else {
                        System.err.println("Failed to get file properties: " + res.cause().getMessage());
                        filePromise.fail(res.cause().getMessage());
                    }
                });
            }
        }
        CompositeFuture.all(filePromises.stream().map(Promise::future).collect(Collectors.toList()))
                .onComplete(as -> this.promise.complete());
    }

    private void manageProps(String file, Promise<Void> filePromise, AsyncResult<FileProps> res) {
        FileProps fileProps = res.result();
        if (fileProps.isDirectory()) {
            vertx.deployVerticle(new DirectoryExplorerVerticle(file, filePromise, this.intervals, this.longestFiles, this.results, this.sourceAnalyzer), VerticleDeployUtils.getDeploymentOptions());
        } else {
            exploreFile(file, filePromise);
        }
    }

    private void exploreFile(String file, Promise<Void> filePromise) {
        if (file.endsWith(".java")) {
            vertx.deployVerticle(new LineCounterVerticle(file, filePromise, this.intervals, this.longestFiles, this.results, this.sourceAnalyzer), VerticleDeployUtils.getDeploymentOptions());
        } else {
            filePromise.complete();
        }
    }
}
