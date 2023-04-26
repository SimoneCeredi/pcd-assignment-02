package pcd.assignment.tasks.executors.model;

import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;

import java.util.concurrent.ExecutorService;

public interface TasksModel {
    IntervalLineCounter getIntervalLineCounter();

    LongestFilesQueue getLongestFiles();

    ExecutorService getExecutorService();
}
