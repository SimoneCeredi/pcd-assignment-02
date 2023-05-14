package pcd.assignment.virtual.threads.model.tasks;

import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.source.analyzer.StoppableSourceAnalyzer;
import pcd.assignment.common.utilities.FilesUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ReadLinesTask implements Runnable {
    private final File file;
    private final CompletableFuture<FileInfo> future;
    private final StoppableSourceAnalyzer sourceAnalyzer;

    public ReadLinesTask(File file, CompletableFuture<FileInfo> future, StoppableSourceAnalyzer sourceAnalyzer) {
        this.file = file;
        this.future = future;
        this.sourceAnalyzer = sourceAnalyzer;
    }

    @Override
    public void run() {
        this.future.completeAsync(() -> {
            final long fileLength = FilesUtils.countLines(file);
            return new FileInfo(this.file, fileLength);
        });
    }
}
