package pcd.assignment.reactive.model.data;

import pcd.assignment.common.model.data.FileInfo;
import pcd.assignment.common.model.data.monitor.BaseLongestFiles;
import pcd.assignment.common.model.data.monitor.LongestFiles;

import java.util.*;

public class SimpleLongestFiles extends BaseLongestFiles {

    private static final int DEFAULT_INITIAL_CAPACITY = 10_000;

    public SimpleLongestFiles(int filesToKeep) {
        super(filesToKeep, new PriorityQueue<>(DEFAULT_INITIAL_CAPACITY,
                Comparator.comparingLong(FileInfo::getLineCount)));
    }

    public SimpleLongestFiles(int filesToKeep, Queue<FileInfo> queue) {
        super(filesToKeep, queue);
    }

    @Override
    public LongestFiles getCopy() {
        return new SimpleLongestFiles(filesToKeep, new PriorityQueue<>(queue));
    }

}
