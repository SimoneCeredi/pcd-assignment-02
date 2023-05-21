package pcd.assignment.common.model.data.functions;

import pcd.assignment.common.model.data.results.FileInfo;
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
                fileInfo.getNumberOfLines() > Objects.requireNonNull(this.queue.peek()).getNumberOfLines()) {
            this.queue.offer(fileInfo);
            if (this.queue.size() > this.filesToKeep) {
                this.queue.poll();
            }
        }
    }

    @Override
    public Queue<FileInfo> get() {
        return this.queue;
    }

}
