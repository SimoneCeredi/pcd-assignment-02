package pcd.assignment.tasks.executors.model.data.monitor;

import pcd.assignment.tasks.executors.model.data.FileInfo;

import java.util.Collection;

public interface LongestFilesQueue {
    void put(FileInfo fileInfo);

    Collection<FileInfo> get();
}
