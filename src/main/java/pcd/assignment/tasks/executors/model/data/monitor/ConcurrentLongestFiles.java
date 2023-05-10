package pcd.assignment.tasks.executors.model.data.monitor;

import pcd.assignment.tasks.executors.model.data.FileInfo;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class  ConcurrentLongestFiles extends BaseLongestFiles {

    public ConcurrentLongestFiles(int filesToKeep) {
        super(filesToKeep,
                new PriorityBlockingQueue<>(filesToKeep + 1, Comparator.comparingLong(FileInfo::getLineCount)));
    }

    public ConcurrentLongestFiles(int filesToKeep, Queue<FileInfo> queue) {
        super(filesToKeep, queue);
    }

    @Override
    public LongestFiles getCopy() {
        return new ConcurrentLongestFiles(filesToKeep, new PriorityBlockingQueue<>(queue));
    }

}
