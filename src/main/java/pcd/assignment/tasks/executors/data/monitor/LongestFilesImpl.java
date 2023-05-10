package pcd.assignment.tasks.executors.data.monitor;

import pcd.assignment.tasks.executors.data.FileInfo;

import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

public class LongestFilesImpl implements LongestFiles {
    private final int filesToKeep;
    private final Queue<FileInfo> queue;

    public LongestFilesImpl(int filesToKeep, Queue<FileInfo> queue) {
        this.filesToKeep = filesToKeep;
        this.queue = queue;
    }

    public LongestFilesImpl(int filesToKeep) {
        this.filesToKeep = filesToKeep;
        this.queue = new PriorityQueue<>(Comparator.comparingLong(FileInfo::getLineCount));
    }

    @Override
    public synchronized void put(FileInfo fileInfo) {
        unSyncPut(fileInfo);
    }

    private void unSyncPut(FileInfo fileInfo) {
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
        filesQueue.get().forEach(this::unSyncPut);
    }

    @Override
    public int getFilesToKeep() {
        return filesToKeep;
    }

    @Override
    public Queue<FileInfo> get() {
        return this.queue;
    }

    @Override
    public LongestFiles getCopy() {
        return new LongestFilesImpl(this.filesToKeep, new PriorityQueue<>(this.queue));
    }
}
