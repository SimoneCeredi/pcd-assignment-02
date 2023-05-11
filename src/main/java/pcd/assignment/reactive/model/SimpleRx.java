package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.reactive.model.data.SimpleIntervals;
import pcd.assignment.reactive.model.data.SimpleLongestFiles;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleRx {

    public static void main(String[] args) throws InterruptedException {

        String rootPath = "./benchmarks/fs/";

        Subject<FileInfo> functionsCalc = PublishSubject.create();
        LongestFilesIntervalsConsumer functionsConsumer =
                new LongestFilesIntervalsConsumer(
                        new SimpleIntervals(10, 10_000),
                        new SimpleLongestFiles(10));


        Observable<File> explorerManager = Observable.create(new ExplorerManager(rootPath, functionsCalc));
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
                .doOnComplete(() -> SimpleRx.log("End!"))
                .blockingSubscribe(functionsConsumer);
    }

    public static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
    }

}
