package pcd.assignment.common.model.data.functions;

import pcd.assignment.common.model.data.results.FileInfo;

/**
 * Intervals model
 */
public interface Intervals extends UnmodifiableIntervals {

    void store(FileInfo fileInfo);

    int getIntervals();

    Intervals getCopy();

}
