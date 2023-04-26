package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.TasksModel;
import pcd.assignment.tasks.executors.model.data.FileInfo;

public class DataManagerTask implements Runnable {
    private final FileInfo fileInfo;
    private final TasksModel model;

    public DataManagerTask(FileInfo fileInfo, TasksModel model) {
        this.fileInfo = fileInfo;
        this.model = model;
    }

    @Override
    public void run() {
        this.model.getIntervalLineCounter().store(this.fileInfo);
        this.model.getLongestFiles().put(this.fileInfo);
    }
}
