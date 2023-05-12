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
        dfs(new ArrayList<>(List.of(this.rootDirectory)));
        emitter.onComplete();
    }

    private void dfs(List<File> nodes) {
        if (nodes.size() > 0) {
            // Explore first directory
            var exploringNode = nodes.remove(0);
            var subnodes = DirectoryExplorerUtils.listDirectories(exploringNode);
            // Insert all subdirs into the head
            if (subnodes.size() > 0) {
                for (int i = 0; i < subnodes.size(); i++) {
                    nodes.add(i, subnodes.get(i));
                }
            } else {
                this.emitter.onNext(exploringNode);
            }
            dfs(nodes);
        }
    }

}
