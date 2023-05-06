package pcd.assignment.tasks.executors.model.data.monitor;

import pcd.assignment.tasks.executors.model.data.FileInfo;

import java.util.Collection;

/**
 * Represents an Unmodifiable {@link LongestFiles}
 */
public interface UnmodifiableLongestFiles {
    Collection<FileInfo> get();

}
