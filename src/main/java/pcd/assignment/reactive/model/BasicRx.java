package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.tasks.executors.model.data.FileInfo;

import java.io.File;

public class BasicRx {

    public static void main(String[] args) throws InterruptedException {

        Observable<String> masterExplorer = Observable.create(emitter -> {
            String file = "./build.gradle.kts";
            log("Sending 100 times " + file);
            for (int i = 0; i < 100; i++) {
                emitter.onNext(file + i);
            }
            emitter.onComplete();
        });

        masterExplorer
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s -> {

                    Observable<String> recursiveExplorer = Observable.create(emitter -> {
                        for (int k = 0; k < 10; k++) {
                            log("Where am I?");
                            emitter.onNext(s + "_" + k);
                        }
                        emitter.onComplete();
                    });

                    Subject<String> sourcesAnalyzer = PublishSubject.create();
                    sourcesAnalyzer.observeOn(Schedulers.single())
                            .subscribeOn(Schedulers.computation())
                            .subscribe(r -> {
                                log("Inside sources analyzer");
                                new FileInfo(new File("build.gradle.kts"), 10);
                            });

                    recursiveExplorer
                            .observeOn(Schedulers.computation())
                            .subscribeOn(Schedulers.io())
                            .subscribe(r -> {
                                log("Sending file to analyzer");
                                sourcesAnalyzer.onNext(r);
                            });
                });

        Thread.sleep(2000);
    }

    static private void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
    }

}
