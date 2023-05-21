package pcd.assignment.common.model.data.results;

import java.io.File;

/**
 * Stores the length of a file
 */
public class FileInfo {
    private final File file;
    private final long numberOfLines;

    public FileInfo(File file, long numberOfLines) {
        this.file = file;
        this.numberOfLines = numberOfLines;
    }

    public File getFile() {
        return this.file;
    }

    public long getNumberOfLines() {
        return this.numberOfLines;
    }
}