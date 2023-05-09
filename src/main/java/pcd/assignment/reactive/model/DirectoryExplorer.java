package pcd.assignment.reactive.model;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.subjects.Subject;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.utilities.FilesUtils;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryExplorer {

    public static Pair<List<File>, List<FileInfo>> exploreDirectory(File directory) {
        List<File> subdirectories = new ArrayList<>();
        List<FileInfo> fileInfos = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        subdirectories.add(file);
                    } else {
                        if (file.getName().endsWith(".java")) {
                            BasicRx.log(file.toString());
                            long fileLength = FilesUtils.countLines(file);
                            fileInfos.add(new FileInfo(file, fileLength));
                        }
                    }
                }
            }
        }
        return new Pair<>(subdirectories, fileInfos);
    }

}
