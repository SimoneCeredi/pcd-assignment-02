package pcd.assignment.reactive.model;

import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.monitor.BasicLongestFiles;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFiles;

import java.util.*;

public class SimpleLongestFiles extends BasicLongestFiles {

    public SimpleLongestFiles(int filesToKeep) {
        super(filesToKeep, new PriorityQueue<>(Comparator.comparingLong(FileInfo::getLineCount)));
    }

}
