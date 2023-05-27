package pcd.assignment.base.model.data.functions;

import pcd.assignment.base.model.data.results.FileInfo;

/**
 * Longest files model: store top N files
 */
public interface LongestFiles extends UnmodifiableLongestFiles {

    void put(FileInfo fileInfo);

    LongestFiles getCopy();

}
