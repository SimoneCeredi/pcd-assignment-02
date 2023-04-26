package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.concurrent.Future;

public interface Model {

    Future<Pair<IntervalLineCounter, LongestFilesQueue>> getReport(File directory);
}
