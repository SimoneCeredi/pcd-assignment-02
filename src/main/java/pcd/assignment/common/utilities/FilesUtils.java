package pcd.assignment.common.utilities;

import pcd.assignment.tasks.executors.data.FileInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FilesUtils {

    public static long countLines(File file) {
        try {
            return new BufferedReader(new FileReader(file.getAbsolutePath())).lines().count();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
