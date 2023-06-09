package pcd.assignment.impl.reactive.model.data;

import pcd.assignment.base.model.data.results.FileInfo;
import pcd.assignment.base.model.data.functions.BaseLongestFiles;
import pcd.assignment.base.model.data.functions.LongestFiles;

import java.util.*;

public class SimpleLongestFiles extends BaseLongestFiles {

    private static final int DEFAULT_INITIAL_CAPACITY = 10_000;

    public SimpleLongestFiles(int filesToKeep) {
        super(filesToKeep, new PriorityQueue<>(DEFAULT_INITIAL_CAPACITY,
                Comparator.comparingLong(FileInfo::getNumberOfLines)));
    }

    public SimpleLongestFiles(int filesToKeep, Queue<FileInfo> queue) {
        super(filesToKeep, queue);
    }

    @Override
    public LongestFiles getCopy() {
        return new SimpleLongestFiles(filesToKeep, new PriorityQueue<>(queue));
    }

}
