package pcd.assignment.base.utils;

import java.io.*;

/**
 * FileUtils class.
 */
public class FilesUtils {

    public static final String DEFAULT_FS_PATH = "benchmarks/fs";

    /**
     * Get the number of lines of a file.
     * @param file
     * @return A long representing the number of lines.
     */
    public static long countLines(File file) {
        try {
            return new BufferedReader(new FileReader(file.getAbsolutePath())).lines().count();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
