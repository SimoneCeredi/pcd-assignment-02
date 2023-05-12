package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.reactive.source.analyzer.SourceAnalyzerImpl;
import pcd.assignment.reactive.tmp.Rx;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;
import pcd.assignment.common.model.data.FileInfo;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecursiveExplorer implements ObservableOnSubscribe<FileInfo> {

    private final File directory;
    private final List<File> excluded;
    private ObservableEmitter<FileInfo> emitter;

    public RecursiveExplorer(File directory, List<File> excluded) {
        this.directory = directory;
        this.excluded = excluded;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<FileInfo> emitter) throws Throwable {
        this.emitter = emitter;
        List<FileInfo> files = DirectoryExplorerUtils.listFiles(this.directory);
        emitFileInfos(files);

        /*while (subdirectories.size() > 0) {
            files = DirectoryExplorerUtils.exploreDirectory(subdirectories.remove(0));
            emitFileInfos(files.getY());
            subdirectories.addAll(filterExcluded(files.getX()));
        }*/
        emitter.onComplete();
    }

    private List<File> filterExcluded(List<File> directories) {
        return directories.stream()
                .filter(d -> !excluded.contains(d))
                .collect(Collectors.toList());
    }

    private void emitFileInfos(List<FileInfo> fileInfos) {
        fileInfos.forEach(fileInfo -> this.emitter.onNext(fileInfo));
    }


}
