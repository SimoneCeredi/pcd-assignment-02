package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.common.model.data.FileInfo;


import java.io.File;

public class SimpleRx {

    public static void main(String[] args) throws InterruptedException {

        String rootPath = "./benchmarks/fs/";

        Subject<FileInfo> maxFileIntervalsComputer = PublishSubject.create();
        maxFileIntervalsComputer
                .observeOn(Schedulers.single())
                .subscribe(r -> {
                    // Here I reactively compute the maximum file in length and intervals
                    // All msg must be received on the same thread
                    // log("I should update maxfile and interval");
                });

        Observable<File> explorerManager = Observable.create(new ExplorerManager(rootPath, maxFileIntervalsComputer));

        explorerManager
                // Since it reads files (I/O operations), run the observer using the io() scheduler
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(s -> {

                    // Recursively compute all FileInfo(s) from the directory specified by the manager
                    Observable<FileInfo> recursiveExplorer =
                            Observable.create(new RecursiveExplorer(s));

                    // Same as explorer manager
                    recursiveExplorer
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.computation())
                            .subscribe(maxFileIntervalsComputer::onNext);
                });

        // This shouldn't be a problem if the GUI is up (?)
        Thread.sleep(30_000);
    }

    public static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
    }

}
