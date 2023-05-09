package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.tasks.executors.model.data.FileInfo;

import java.io.File;

public class BasicRx {

    public static void main(String[] args) throws InterruptedException {

        String rootPath = "./";

        Subject<FileInfo> sourcesAnalyzer = PublishSubject.create();
        sourcesAnalyzer
                .observeOn(Schedulers.single())
                .subscribe(r -> {
                    new FileInfo(new File("build.gradle.kts"), 10);
                });

        Observable<File> explorerManager = Observable.create(new ExplorerManager(rootPath, sourcesAnalyzer));

        explorerManager
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s -> {

                    Observable<FileInfo> recursiveExplorer =
                            Observable.create(new RecursiveExplorer(s));

                    recursiveExplorer
                            .observeOn(Schedulers.computation())
                            .subscribeOn(Schedulers.io())
                            .subscribe(r -> {
                                sourcesAnalyzer.onNext(r);
                            });
                });

        Thread.sleep(2000);
    }

    public static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
    }

}
