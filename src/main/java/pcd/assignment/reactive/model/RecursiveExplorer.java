package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.reactive.source.analyzer.SourceAnalyzerImpl;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;
import pcd.assignment.common.model.data.FileInfo;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecursiveExplorer implements ObservableOnSubscribe<FileInfo> {

    private final File directory;
    private ObservableEmitter<FileInfo> emitter;

    public RecursiveExplorer(File directory) {
        this.directory = directory;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<FileInfo> emitter) throws Throwable {
        this.emitter = emitter;
        List<FileInfo> files = DirectoryExplorerUtils.listFiles(this.directory);
        emitFileInfos(files);
        emitter.onComplete();
    }

    private void emitFileInfos(List<FileInfo> fileInfos) {
        fileInfos.forEach(fileInfo -> this.emitter.onNext(fileInfo));
    }

}
