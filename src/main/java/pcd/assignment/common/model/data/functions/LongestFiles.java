package pcd.assignment.common.model.data.functions;

import pcd.assignment.common.model.data.results.FileInfo;

/**
 * Stores the TopN files
 */
public interface LongestFiles extends UnmodifiableLongestFiles {
    void put(FileInfo fileInfo);

    void putAll(LongestFiles filesQueue);

    int getFilesToKeep();

    LongestFiles getCopy();
}
