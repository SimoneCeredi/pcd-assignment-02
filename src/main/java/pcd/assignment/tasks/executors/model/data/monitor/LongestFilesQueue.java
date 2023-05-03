package pcd.assignment.tasks.executors.model.data.monitor;

import pcd.assignment.tasks.executors.model.data.FileInfo;

public interface LongestFilesQueue extends UnmodifiableLongestFilesQueue {
    void put(FileInfo fileInfo);

    void putAll(LongestFilesQueue filesQueue);

    int getFilesToKeep();


    LongestFilesQueue getCopy();
}
