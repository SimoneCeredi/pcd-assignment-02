package pcd.assignment.tasks.executors.model.data.monitor;

import pcd.assignment.tasks.executors.model.data.FileInfo;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class ConcurrentLongestFiles extends BasicLongestFiles {

    public ConcurrentLongestFiles(int filesToKeep) {
        super(filesToKeep,
                new PriorityBlockingQueue<>(0, Comparator.comparingLong(FileInfo::getLineCount)));
    }
}
