package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;
import pcd.assignment.common.model.data.FileInfo;


import java.io.File;
import java.util.List;

public class RecursiveExplorer implements ObservableOnSubscribe<FileInfo> {

    private final File directory;
    private ObservableEmitter<FileInfo> emitter;

    public RecursiveExplorer(File directory) {
        this.directory = directory;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<FileInfo> emitter) throws Throwable {
        this.emitter = emitter;
        List<FileInfo> fileInfos = DirectoryExplorerUtils.listFiles(this.directory);
        fileInfos.forEach(fileInfo -> this.emitter.onNext(fileInfo));
        emitter.onComplete();
    }


}
