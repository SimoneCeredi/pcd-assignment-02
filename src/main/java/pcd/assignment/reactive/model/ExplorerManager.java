package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.reactive.source.analyzer.SourceAnalyzerImpl;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;
import pcd.assignment.common.model.data.FileInfo;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExplorerManager implements ObservableOnSubscribe<File> {

    private final File rootDirectory;
    private ObservableEmitter<File> emitter;

    public ExplorerManager(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<File> emitter) {
        this.emitter = emitter;
        bfs(new ArrayList<>(List.of(this.rootDirectory)));
        emitter.onComplete();
    }

    private void bfs(List<File> nodes) {
        if (nodes.size() > 0) {
            nodes.forEach(n -> this.emitter.onNext(n));
            List<File> subdirs = new ArrayList<>();
            for (File node : nodes) {
                subdirs.addAll(DirectoryExplorerUtils.listDirectories(node));
            }
            bfs(subdirs);
        }
    }
}
