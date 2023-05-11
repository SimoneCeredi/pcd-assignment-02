package pcd.assignment.reactive.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import pcd.assignment.common.utilities.FilesUtils;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.reactive.model.SimpleRx;
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
                            try {
                                long fileLength =
                                        new BufferedReader(new FileReader(file.getAbsolutePath())).lines().count();
                                fileInfos.add(new FileInfo(file, fileLength));
                                SimpleRx.log(file + ": " + fileLength);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
        return new Pair<>(subdirectories, fileInfos);
    }

}
