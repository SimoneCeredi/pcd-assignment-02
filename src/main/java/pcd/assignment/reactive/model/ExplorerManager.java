package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;
import pcd.assignment.common.model.data.FileInfo;


import java.io.File;
import java.util.List;

public class ExplorerManager implements ObservableOnSubscribe<File> {

    private String rootPath;
    private Subject<FileInfo> sourcesAnalyzer;
    private static final int MAX_STREAMS = 20;

    public ExplorerManager(String rootPath, Subject<FileInfo> sourcesAnalyzer) {
        this.rootPath = rootPath;
        this.sourcesAnalyzer = sourcesAnalyzer;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter emitter) {
        SimpleRx.log("Hello, I'm the manager");

        File rootDirectory = new File(this.rootPath);
        Pair<List<File>, List<FileInfo>> content = DirectoryExplorerUtils.exploreDirectory(rootDirectory);
        onNextAnalyze(content.getY());
        List<File> subdirectories = content.getX();

        int currentNStreams = 1;
        while (subdirectories.size() > 0) {
            File firstSubdirectory = subdirectories.remove(0);
            if (true){//currentNStreams < MAX_STREAMS) {
                emitter.onNext(firstSubdirectory);
                currentNStreams++;
            } else {
                content = DirectoryExplorerUtils.exploreDirectory(firstSubdirectory);
                onNextAnalyze(content.getY());
                subdirectories.addAll(content.getX());
            }
        }
        emitter.onComplete();
    }

    private void onNextAnalyze(List<FileInfo> fileInfos) {
        fileInfos.forEach(file -> sourcesAnalyzer.onNext(file));
    }

}
