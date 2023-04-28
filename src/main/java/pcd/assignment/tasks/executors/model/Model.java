package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.utilities.Pair;

import java.io.File;

public interface Model {

    Pair<IntervalLineCounter, LongestFilesQueue> getReport(File directory);
}
