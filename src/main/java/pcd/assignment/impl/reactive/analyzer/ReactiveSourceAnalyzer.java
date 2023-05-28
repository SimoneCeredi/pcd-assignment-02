package pcd.assignment.impl.reactive.analyzer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.base.model.Model;
import pcd.assignment.base.model.data.functions.ConcurrentIntervals;
import pcd.assignment.base.model.data.functions.ConcurrentLongestFiles;
import pcd.assignment.base.model.data.results.FileInfo;
import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.base.model.data.results.ResultsDataImpl;
import pcd.assignment.base.analyzer.SourceAnalyzer;
import pcd.assignment.impl.reactive.model.FileSystemExplorer;
import pcd.assignment.impl.reactive.model.FunctionsConsumer;
import pcd.assignment.impl.reactive.model.LineReader;
import pcd.assignment.impl.reactive.model.data.SimpleIntervals;
import pcd.assignment.impl.reactive.model.data.SimpleLongestFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reactive version of SourceAnalyzer.
 */
public class ReactiveSourceAnalyzer implements SourceAnalyzer {

    private final Model model;
    private ResultsData resultsData;

    public ReactiveSourceAnalyzer(Model model) {
        this.model = model;
    }

    /**
     * Reactive analyzeSources() builds a set of stream responsible for explore the file system,
     * read all the files information and update the results (ResultsData).
     * @param directory
     * @return
     */
    @Override
    public ResultsData analyzeSources(File directory) {
        this.resultsData = new ResultsDataImpl();
        chain(directory);
        return this.resultsData;
    }

    /**
     * The chain is a concat of explorerChain() and functionsChain().
     * @param directory
     */
    private void chain(File directory){
        Subject<FileInfo> functionsCalculator = PublishSubject.create();
        Observable<File> explorerManager =
                Observable.create(new FileSystemExplorer(directory, resultsData));
        explorerChain(explorerManager, functionsCalculator);
        functionsChain(functionsCalculator);
    }

    /**
     * Builds the explorer chain.
     *      - For every directory emitted from the manager (FileSystemExplorer), create a new LineReader Observable
     *          (inside subscribe()) which emits a FileInfo (<file, numberOfLines>) for each file.
     *      - Each LineReader subscriber emits a new FileInfo inside the FunctionsCalculator stream, which
     *          interactively computes the Intervals and LongestFiles functions inside a single thread.
     * @param explorerManager which uses FileSystemExplorer
     * @param functionsCalculator to emit the FileInfo(s)
     */
    private void explorerChain(Observable<File> explorerManager,
                               Subject<FileInfo> functionsCalculator) {
        List<Observable<FileInfo>> lineReaders = new ArrayList<>();
        explorerManager
                .subscribeOn(Schedulers.io())
                .doOnComplete(() ->
                        Observable.merge(lineReaders)
                                .doOnComplete(functionsCalculator::onComplete)
                                .subscribe(functionsCalculator::onNext)
                )
                .subscribe(d -> {
                    Observable<FileInfo> lineReader = Observable.create(new LineReader(d, resultsData))
                            .subscribeOn(Schedulers.io());
                    lineReaders.add(lineReader);
                });
    }

    /**
     * For every FileInfo emitted from the LineReaders, a FunctionsConsumer updates the Intervals
     * and LongestFiles of a ResultsData.
     * @param functionsCalculator
     */
    private void functionsChain(Subject<FileInfo> functionsCalculator) {
        Consumer<FileInfo> functionsConsumer =
                new FunctionsConsumer(
                        new SimpleIntervals(model.getConfiguration().getNumberOfIntervals(),
                                model.getConfiguration().getMaximumLines()),
                        new SimpleLongestFiles(model.getConfiguration().getAtMostNFiles()),
                        resultsData.getResults());
        functionsCalculator
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .doOnComplete(() -> {
                    if (!this.resultsData.isStopped()) {
                        resultsData.getCompletionFuture().complete(null);
                    }
                })
                .subscribe(functionsConsumer);
    }

}
