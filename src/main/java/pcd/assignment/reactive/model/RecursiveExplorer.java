package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;
import pcd.assignment.common.model.data.FileInfo;


import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecursiveExplorer implements ObservableOnSubscribe<FileInfo> {

    private final File directory;
    private final AtomicBoolean stop;

    public RecursiveExplorer(File directory, AtomicBoolean stop) {
        this.directory = directory;
        this.stop = stop;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<FileInfo> emitter) throws Throwable {
        List<FileInfo> fileInfos = DirectoryExplorerUtils.listFiles(this.directory);
        for (int i = 0; i < fileInfos.size() && !this.stop.get(); i++) {
            emitter.onNext(fileInfos.get(i));
        }
        emitter.onComplete();
    }


}
