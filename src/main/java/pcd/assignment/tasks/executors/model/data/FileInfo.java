package pcd.assignment.tasks.executors.model.data;

import java.io.File;

public class FileInfo {
    private final File file;
    private final int lineCount;

    public FileInfo(File file, int lineCount) {
        this.file = file;
        this.lineCount = lineCount;
    }

    public File getFile() {
        return this.file;
    }

    public int getLineCount() {
        return this.lineCount;
    }
}