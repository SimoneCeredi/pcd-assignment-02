package pcd.assignment.common.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FilesUtils {
    public static long countLines(File file) {
        try (var lines = Files.lines(file.toPath())) {
            return lines.count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static List<File> getFiles(String folderPath) {
        List<File> files = new ArrayList<>();
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] subFiles = folder.listFiles();
            if (subFiles != null) {
                for (File file : subFiles) {
                    if (file.isDirectory()) {
                        files.addAll(getFiles(file.getAbsolutePath()));
                    } else {
                        if (file.getName().endsWith(".java")) {
                            files.add(file);
                        }
                    }
                }
            }
        }
        return files;
    }
}
