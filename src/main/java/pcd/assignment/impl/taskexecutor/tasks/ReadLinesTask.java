package pcd.assignment.impl.taskexecutor.tasks;

import pcd.assignment.base.utils.FilesUtils;
import pcd.assignment.base.model.data.results.FileInfo;

import java.io.File;
import java.util.concurrent.RecursiveTask;

/**
 * Read lines task
 */
public class ReadLinesTask extends RecursiveTask<FileInfo> {
    private final File file;

    public ReadLinesTask(File file) {
        this.file = file;
    }

    /**
     * @return FileInfo of this.file
     */
    @Override
    protected FileInfo compute() {
        final long fileLength = FilesUtils.countLines(file);
        return new FileInfo(this.file, fileLength);
    }
}
