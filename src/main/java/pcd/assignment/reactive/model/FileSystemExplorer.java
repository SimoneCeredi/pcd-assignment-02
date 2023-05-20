package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import pcd.assignment.common.model.data.results.ResultsData;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExplorerManager implements ObservableOnSubscribe<File> {

    private final File rootDirectory;
    private final ResultsData resultsData;
    private ObservableEmitter<File> emitter;

    public ExplorerManager(File rootDirectory, ResultsData resultsData) {
        this.rootDirectory = rootDirectory;
        this.resultsData = resultsData;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<File> emitter) {
        this.emitter = emitter;
        bfs(new ArrayList<>(List.of(this.rootDirectory)));
        emitter.onComplete();
    }

    private void bfs(List<File> nodes) {
        if (nodes.size() > 0 && !this.resultsData.isStopped()) {
            List<File> subdirectories = new ArrayList<>();
            for (File node : nodes) {
                if (this.resultsData.isStopped()) {
                    return;
                }
                this.emitter.onNext(node);
                subdirectories.addAll(DirectoryExplorerUtils.listDirectories(node));
            }
            bfs(subdirectories);
        }
    }
}
