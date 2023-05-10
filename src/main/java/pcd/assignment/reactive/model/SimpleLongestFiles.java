package pcd.assignment.reactive.model;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.monitor.BaseLongestFiles;
import pcd.assignment.tasks.executors.model.data.monitor.ConcurrentLongestFiles;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFiles;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class SimpleLongestFiles extends BaseLongestFiles {

    public SimpleLongestFiles(int filesToKeep) {
        super(filesToKeep, new PriorityQueue<>(filesToKeep + 1, Comparator.comparingLong(FileInfo::getLineCount)));
    }

    public SimpleLongestFiles(int filesToKeep, Queue<FileInfo> queue) {
        super(filesToKeep, queue);
    }

    @Override
    public LongestFiles getCopy() {
        return new SimpleLongestFiles(filesToKeep, new PriorityQueue<>(queue));
    }

}
