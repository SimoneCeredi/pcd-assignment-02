package pcd.assignment.tasks.executors.model.data;

import java.io.File;

public class FileInfo {
    private final File file;
    private final long lineCount;

    public FileInfo(File file, long lineCount) {
        this.file = file;
        this.lineCount = lineCount;
    }

    public File getFile() {
        return this.file;
    }

    public long getLineCount() {
        return this.lineCount;
    }
}