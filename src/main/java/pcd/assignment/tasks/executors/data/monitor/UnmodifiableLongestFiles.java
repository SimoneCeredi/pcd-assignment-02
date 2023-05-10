package pcd.assignment.tasks.executors.data.monitor;

import pcd.assignment.tasks.executors.data.FileInfo;

import java.util.Collection;

/**
 * Represents an Unmodifiable {@link LongestFiles}
 */
public interface UnmodifiableLongestFiles {
    Collection<FileInfo> get();

}
