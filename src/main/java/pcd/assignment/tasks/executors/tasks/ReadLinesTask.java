package pcd.assignment.tasks.executors.tasks;

import pcd.assignment.common.utilities.FilesUtils;
import pcd.assignment.common.model.data.results.FileInfo;

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
