package pcd.assignment.common.model.data.monitor;

import pcd.assignment.common.model.data.FileInfo;
import java.util.Objects;
import java.util.Queue;

public abstract class BaseLongestFiles implements LongestFiles {

    protected final int filesToKeep;
    protected final Queue<FileInfo> queue;

    public BaseLongestFiles(int filesToKeep, Queue<FileInfo> queue) {
        this.filesToKeep = filesToKeep;
        this.queue = queue;
    }

    @Override
    public void put(FileInfo fileInfo) {
        if (this.queue.size() < this.filesToKeep ||
                fileInfo.getLineCount() > Objects.requireNonNull(this.queue.peek()).getLineCount()) {
            this.queue.offer(fileInfo);
            if (this.queue.size() > this.filesToKeep) {
                this.queue.poll();
            }
        }
    }

    @Override
    public void putAll(LongestFiles filesQueue) {
        filesQueue.get().forEach(this::put);
    }

    @Override
    public int getFilesToKeep() {
        return filesToKeep;
    }

    @Override
    public Queue<FileInfo> get() {
        return this.queue;
    }

}
