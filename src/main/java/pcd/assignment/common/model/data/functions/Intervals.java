package pcd.assignment.common.model.data.functions;

import pcd.assignment.common.model.data.results.FileInfo;

/**
 * Stores the intervals
 */
public interface Intervals extends UnmodifiableIntervals {
    void store(FileInfo fileInfo);

    void storeAll(Intervals lineCounter);

    int getIntervals();

    int getMaxLines();

    Intervals getCopy();
}
