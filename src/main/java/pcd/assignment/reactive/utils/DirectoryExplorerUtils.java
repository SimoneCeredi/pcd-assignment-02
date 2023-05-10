package pcd.assignment.reactive.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pcd.assignment.common.utilities.FilesUtils;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.FileInfo;


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
                            //SimpleRx.log(file.toString());
                            try {
                                long fileLength = FilesUtils.countLines(file);
                                fileInfos.add(new FileInfo(file, fileLength));
                            } catch (Exception ignored) {}

                        }
                    }
                }
            }
        }
        return new Pair<>(subdirectories, fileInfos);
    }

}
