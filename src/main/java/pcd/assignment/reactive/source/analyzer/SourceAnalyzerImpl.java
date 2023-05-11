package pcd.assignment.reactive.source.analyzer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.reactive.model.ExplorerManager;
import pcd.assignment.reactive.model.LongestFilesIntervalsConsumer;
import pcd.assignment.reactive.model.RecursiveExplorer;
import pcd.assignment.reactive.model.data.SimpleIntervals;
import pcd.assignment.reactive.model.data.SimpleLongestFiles;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceAnalyzerImpl implements SourceAnalyzer {

    private final Model model;

    public SourceAnalyzerImpl(Model model) {
        this.model = model;
    }

    public static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
    }

    @Override
    public Pair<BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>>, CompletableFuture<Void>> analyzeSources(File directory) {

        // Return data structures
        BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results = new LinkedBlockingQueue<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        Subject<FileInfo> functionsCalc = PublishSubject.create();
        LongestFilesIntervalsConsumer functionsConsumer =
                new LongestFilesIntervalsConsumer(
                        new SimpleIntervals(model.getNi(), model.getMaxl()),
                        new SimpleLongestFiles(model.getN()),
                        results);

        Observable<File> explorerManager = Observable.create(new ExplorerManager(directory, functionsCalc));
        AtomicInteger counter = new AtomicInteger();
        explorerManager
                // Since it reads files (I/O operations), run the observer using the io() scheduler
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(s -> {

                    // Recursively compute all FileInfo(s) from the directory specified by the manager
                    Observable<FileInfo> recursiveExplorer =
                            Observable.create(new RecursiveExplorer(s));
                    counter.getAndIncrement();
                    // Same as explorer manager
                    recursiveExplorer
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.single())
                            .doOnComplete(() -> {
                                counter.getAndDecrement();
                                if (counter.get() == 0) {
                                    functionsCalc.onComplete();
                                }
                            })
                            .subscribe(functionsCalc::onNext);
                });

        functionsCalc
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.single())
                .doOnComplete(() -> {
                    //System.out.println("End!");
                    future.complete(null);
                })
                .subscribe(functionsConsumer);
                //.blockingSubscribe(functionsConsumer);
        return new Pair<>(results, future);
    }

    @Override
    public void stop() {

    }
}
