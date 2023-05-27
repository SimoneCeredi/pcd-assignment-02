package pcd.assignment.base.model.data.functions;

import pcd.assignment.base.model.data.results.FileInfo;

/**
 * Intervals model
 */
public interface Intervals extends UnmodifiableIntervals {

    void store(FileInfo fileInfo);

    int getIntervals();

    Intervals getCopy();

}
