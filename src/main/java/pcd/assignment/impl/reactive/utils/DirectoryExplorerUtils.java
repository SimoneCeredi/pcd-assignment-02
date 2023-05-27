package pcd.assignment.impl.reactive.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pcd.assignment.base.utilities.FilesUtils;
import pcd.assignment.base.model.data.results.FileInfo;

/**
 * Directory explorer utils to list file system's.
 */
public class DirectoryExplorerUtils {

    public static List<FileInfo> listJavaFiles(File directory) {
        List<FileInfo> fileInfos = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isDirectory()) {
                        if (file.getName().endsWith(".java") && file.canRead()) {
                            long fileLength =
                                    FilesUtils.countLines(file);
                            fileInfos.add(new FileInfo(file, fileLength));
                        }
                    }
                }
            }
        }
        return fileInfos;
    }

    public static List<File> listDirectories(File directory) {
        List<File> subdirectories = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        subdirectories.add(file);
                    }
                }
            }
        }
        return subdirectories;
    }

}
