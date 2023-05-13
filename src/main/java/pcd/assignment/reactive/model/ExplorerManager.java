package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExplorerManager implements ObservableOnSubscribe<File> {

    private final File rootDirectory;
    private final AtomicBoolean stop;
    private ObservableEmitter<File> emitter;

    public ExplorerManager(File rootDirectory, AtomicBoolean stop) {
        this.rootDirectory = rootDirectory;
        this.stop = stop;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<File> emitter) {
        this.emitter = emitter;
        bfs(new ArrayList<>(List.of(this.rootDirectory)));
        emitter.onComplete();
    }

    private void bfs(List<File> nodes) {
        if (nodes.size() > 0 && !stop.get()) {
            nodes.forEach(n -> this.emitter.onNext(n));
            List<File> subdirs = new ArrayList<>();
            for (File node : nodes) {
                if (stop.get()) {
                    return;
                }
                subdirs.addAll(DirectoryExplorerUtils.listDirectories(node));
            }
            bfs(subdirs);
        }
    }
}
