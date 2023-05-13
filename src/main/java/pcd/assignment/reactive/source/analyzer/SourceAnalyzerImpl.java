package pcd.assignment.reactive.source.analyzer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
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
import pcd.assignment.reactive.model.FunctionsConsumer;
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
        BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results =
                new LinkedBlockingQueue<>();
        CompletableFuture<Void> future = new CompletableFuture<>();
        chain(results, future, directory);
        return new Pair<>(results, future);
    }

    private void chain(BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results,
                       CompletableFuture<Void> future,
                       File directory){
        Subject<FileInfo> functionsCalculator = PublishSubject.create();
        Observable<File> explorerManager =
                Observable.create(new ExplorerManager(directory));
        explorerChain(explorerManager, functionsCalculator);
        functionsChain(functionsCalculator, results, future);
    }

    private void explorerChain(Observable<File> explorerManager,
                               Subject<FileInfo> functionsCalculator) {
        AtomicInteger counter = new AtomicInteger();
        explorerManager
                // Since it reads files (I/O operations), run the observer using the io() scheduler
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(d -> {
                    // Recursively compute all FileInfo(s) from the directory specified by the manager
                    Observable<FileInfo> recursiveExplorer =
                            Observable.create(new RecursiveExplorer(d));
                    counter.getAndIncrement();
                    // Same as explorer manager
                    recursiveExplorer
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.single())
                            .doOnComplete(() -> {
                                counter.getAndDecrement();
                                if (counter.get() == 0) {
                                    functionsCalculator.onComplete();
                                }
                            })
                            .subscribe(functionsCalculator::onNext);
                });
    }

    private void functionsChain(Subject<FileInfo> functionsCalculator,
                                BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results,
                                CompletableFuture<Void> future) {
        Consumer<FileInfo> functionsConsumer =
                new FunctionsConsumer(
                        new SimpleIntervals(model.getNi(), model.getMaxl()),
                        new SimpleLongestFiles(model.getN()),
                        results);
        functionsCalculator
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.single())
                .doOnComplete(() -> {
                    //System.out.println("End!");
                    future.complete(null);
                })
                .subscribe(functionsConsumer);
    }

    @Override
    public void stop() {

    }
}
