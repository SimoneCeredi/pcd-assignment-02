package pcd.assignment.common.model.data.results;

import pcd.assignment.common.model.data.functions.Intervals;
import pcd.assignment.common.model.data.functions.LongestFiles;

/**
 * Result model
 */
public interface Result {

    Intervals getIntervals();

    LongestFiles getLongestFiles();

}
