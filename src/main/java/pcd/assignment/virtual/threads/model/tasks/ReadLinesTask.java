package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.source.analyzer.SourceAnalyzerData;
import pcd.assignment.common.utilities.FilesUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ReadLinesTask implements Runnable {
    private final File file;
    private final CompletableFuture<FileInfo> future;
    private final SourceAnalyzerData data;

    public ReadLinesTask(File file, CompletableFuture<FileInfo> future, SourceAnalyzerData data) {
        this.file = file;
        this.future = future;
        this.data = data;
    }

    @Override
    public void run() {
        this.future.completeAsync(() -> {
            final long fileLength = FilesUtils.countLines(file);
            return new FileInfo(this.file, fileLength);
        });
    }
}
