package pcd.assignment.base.model.data.results;

import pcd.assignment.base.model.data.functions.Intervals;
import pcd.assignment.base.model.data.functions.LongestFiles;

/**
 * Result model
 */
public interface Result {

    Intervals getIntervals();

    LongestFiles getLongestFiles();

}
