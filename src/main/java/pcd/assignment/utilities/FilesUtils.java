package pcd.assignment.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FilesUtils {
    public static long countLines(File file) {
        try (var lines = Files.lines(file.toPath())) {
            return lines.count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
