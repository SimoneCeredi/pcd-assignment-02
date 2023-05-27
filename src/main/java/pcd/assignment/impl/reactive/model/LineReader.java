package pcd.assignment.impl.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.impl.reactive.utils.DirectoryExplorerUtils;
import pcd.assignment.base.model.data.results.FileInfo;


import java.io.File;
import java.util.List;

/**
 * This class emits a FileInfo for every Java file in its directory.
 */
public class LineReader implements ObservableOnSubscribe<FileInfo> {

    private final File directory;
    private final ResultsData resultsData;

    public LineReader(File directory, ResultsData resultsData) {
        this.directory = directory;
        this.resultsData = resultsData;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<FileInfo> emitter) {
        List<FileInfo> fileInfos = DirectoryExplorerUtils.listJavaFiles(this.directory);
        for (int i = 0; i < fileInfos.size() && !this.resultsData.isStopped(); i++) {
            emitter.onNext(fileInfos.get(i));
        }
        emitter.onComplete();
    }


}
