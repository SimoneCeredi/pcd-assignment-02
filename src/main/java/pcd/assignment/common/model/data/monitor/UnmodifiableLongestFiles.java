package pcd.assignment.common.model.data.monitor;

import pcd.assignment.common.model.data.FileInfo;

import java.util.Collection;

/**
 * Represents an Unmodifiable {@link LongestFiles}
 */
public interface UnmodifiableLongestFiles {
    Collection<FileInfo> get();

}
