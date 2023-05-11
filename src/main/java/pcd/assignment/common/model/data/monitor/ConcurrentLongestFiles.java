package pcd.assignment.common.model.data.monitor;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import pcd.assignment.common.model.data.FileInfo;

public class  ConcurrentLongestFiles extends BaseLongestFiles {

    private static final int DEFAULT_INITIAL_CAPACITY = 5_000;

    public ConcurrentLongestFiles(int filesToKeep) {
        super(filesToKeep,
                new PriorityBlockingQueue<>(DEFAULT_INITIAL_CAPACITY, Comparator.comparingLong(FileInfo::getLineCount)));
    }

    public ConcurrentLongestFiles(int filesToKeep, Queue<FileInfo> queue) {
        super(filesToKeep, queue);
    }

    @Override
    public LongestFiles getCopy() {
        return new ConcurrentLongestFiles(filesToKeep, new PriorityBlockingQueue<>(queue));
    }

}
