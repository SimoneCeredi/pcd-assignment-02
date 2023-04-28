package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.utilities.FilesUtils;

import java.io.File;
import java.util.concurrent.RecursiveTask;

public class ReadLinesTask extends RecursiveTask<FileInfo> {
    private final File file;

    public ReadLinesTask(File file) {
        this.file = file;
    }

    @Override
    protected FileInfo compute() {
        final long fileLength = FilesUtils.countLines(file);
        return new FileInfo(this.file, fileLength);
    }
}
