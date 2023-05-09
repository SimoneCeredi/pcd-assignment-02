package pcd.assignment.reactive.utils;

import pcd.assignment.reactive.model.SimpleRx;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.utilities.FilesUtils;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryExplorerUtils {

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
                        if (file.getName().endsWith(".java") && file.canRead()) {
                            SimpleRx.log(file.toString());
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
