package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import pcd.assignment.reactive.utils.DirectoryExplorerUtils;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecursiveExplorer implements ObservableOnSubscribe<FileInfo> {

    private File directory;
    private ObservableEmitter<FileInfo> emitter;

    public RecursiveExplorer(File directory) {
        this.directory = directory;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<FileInfo> emitter) throws Throwable {
        this.emitter = emitter;
        SimpleRx.log("Hello, I'm recursive and I start producing");
        List<File> subdirectories = new ArrayList<>(List.of(this.directory));

        Pair<List<File>, List<FileInfo>> content = DirectoryExplorerUtils.exploreDirectory(this.directory);
        emitFileInfos(content.getY());

        while (subdirectories.size() > 0) {
            content = DirectoryExplorerUtils.exploreDirectory(subdirectories.remove(0));
            emitFileInfos(content.getY());
            subdirectories.addAll(content.getX());
        }
        emitter.onComplete();
    }

    private void emitFileInfos(List<FileInfo> fileInfos) {
        fileInfos.forEach(fileInfo -> this.emitter.onNext(fileInfo));
    }


}
