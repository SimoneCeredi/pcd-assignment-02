package pcd.assignment.reactive.analyzer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.results.FileInfo;
import pcd.assignment.common.model.data.results.ResultsData;
import pcd.assignment.common.model.data.results.ResultsDataImpl;
import pcd.assignment.common.analyzer.SourceAnalyzer;
import pcd.assignment.reactive.model.ExplorerManager;
import pcd.assignment.reactive.model.FunctionsConsumer;
import pcd.assignment.reactive.model.RecursiveExplorer;
import pcd.assignment.reactive.model.data.SimpleIntervals;
import pcd.assignment.reactive.model.data.SimpleLongestFiles;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceAnalyzerImpl implements SourceAnalyzer {

    private final Model model;
    private ResultsData resultsData;

    public SourceAnalyzerImpl(Model model) {
        this.model = model;
    }

    @Override
    public ResultsData analyzeSources(File directory) {
        this.resultsData = new ResultsDataImpl();
        chain(directory);
        return this.resultsData;
    }

    private void chain(File directory){
        Subject<FileInfo> functionsCalculator = PublishSubject.create();
        Observable<File> explorerManager =
                Observable.create(new ExplorerManager(directory, resultsData));
        explorerChain(explorerManager, functionsCalculator);
        functionsChain(functionsCalculator);
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
                            Observable.create(new RecursiveExplorer(d, resultsData));
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

    private void functionsChain(Subject<FileInfo> functionsCalculator) {
        Consumer<FileInfo> functionsConsumer =
                new FunctionsConsumer(
                        new SimpleIntervals(model.getConfiguration().getNumberOfIntervals(),
                                model.getConfiguration().getMaximumLines()),
                        new SimpleLongestFiles(model.getConfiguration().getAtMostNFiles()),
                        resultsData.getResults());
        functionsCalculator
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.single())
                .doOnComplete(() -> {

                    if (!this.resultsData.isStopped()) {
                        resultsData.getCompletionFuture().complete(null);
                    }
                    //this.stop.set(true);
                })
                .subscribe(functionsConsumer);
    }

}
