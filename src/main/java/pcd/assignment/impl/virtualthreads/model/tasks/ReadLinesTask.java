package pcd.assignment.impl.virtualthreads.model.tasks;
import pcd.assignment.base.model.data.results.FileInfo;
import pcd.assignment.base.utilities.FilesUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * Read lines task as Runnable
 */
public class ReadLinesTask implements Runnable {

    private final File file;
    private final CompletableFuture<FileInfo> fileInfoFuture;

    public ReadLinesTask(File file, CompletableFuture<FileInfo> fileInfoFuture) {
        this.file = file;
        this.fileInfoFuture = fileInfoFuture;
    }

    /**
     * Completes the CompletableFuture with the FileInfo of the local file.
     */
    @Override
    public void run() {
        this.fileInfoFuture.completeAsync(() -> {
            final long fileLength = FilesUtils.countLines(file);
            return new FileInfo(this.file, fileLength);
        });
    }

}
