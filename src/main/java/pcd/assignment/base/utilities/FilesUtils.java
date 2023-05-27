package pcd.assignment.base.utilities;

import java.io.*;

/**
 * FileUtils class.
 */
public class FilesUtils {

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
