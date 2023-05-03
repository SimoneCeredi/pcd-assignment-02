package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.utilities.FilesUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ReadLinesTask implements Runnable {
    private final File file;
    private final CompletableFuture<FileInfo> future;

    public ReadLinesTask(File file, CompletableFuture<FileInfo> future) {
        this.file = file;
        this.future = future;
    }

    @Override
    public void run() {
        this.future.completeAsync(() -> {
            final long fileLength = FilesUtils.countLines(file);
            return new FileInfo(this.file, fileLength);
        });
    }
}
