package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.FilesUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ReadLinesTask implements Runnable {
    private final File file;
    private final CompletableFuture<FileInfo> fileResFuture;

    public ReadLinesTask(File file, CompletableFuture<FileInfo> fileResFuture) {
        this.file = file;
        this.fileResFuture = fileResFuture;
    }

    @Override
    public void run() {
        this.fileResFuture.completeAsync(() -> {
            final long fileLength = FilesUtils.countLines(file);
            return new FileInfo(this.file, fileLength);
        });
    }
}
