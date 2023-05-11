package pcd.assignment.common.utilities;

import java.io.*;

public class FilesUtils {

    public static long countLines(File file) {
        try {
            return new BufferedReader(new FileReader(file.getAbsolutePath())).lines().count();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
