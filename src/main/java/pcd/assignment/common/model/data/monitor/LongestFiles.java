package pcd.assignment.common.model.data.monitor;

import pcd.assignment.common.model.data.FileInfo;

/**
 * Stores the TopN files
 */
public interface LongestFiles extends UnmodifiableLongestFiles {
    void put(FileInfo fileInfo);

    void putAll(LongestFiles filesQueue);

    int getFilesToKeep();


    LongestFiles getCopy();
}
