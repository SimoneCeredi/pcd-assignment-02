package pcd.assignment.tasks.executors.model.data.monitor;

import pcd.assignment.tasks.executors.model.data.FileInfo;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

public class BasicLongestFiles implements LongestFiles {
    private final int filesToKeep;
    private final Queue<FileInfo> queue;

    public BasicLongestFiles(int filesToKeep, Queue<FileInfo> queue) {
        this.filesToKeep = filesToKeep;
        this.queue = queue;
    }

    /*public LongestFilesImpl(int filesToKeep) {
        this.filesToKeep = filesToKeep;
        this.queue = new PriorityQueue<>(Comparator.comparingLong(FileInfo::getLineCount));
    }
    */

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
    public synchronized Queue<FileInfo> get() {
        return this.queue;
    }

    @Override
    public synchronized LongestFiles getCopy() {
        return new BasicLongestFiles(this.filesToKeep, new PriorityQueue<>(this.queue));
    }
}
