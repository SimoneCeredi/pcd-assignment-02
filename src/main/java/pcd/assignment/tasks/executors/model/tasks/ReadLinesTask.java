package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.TasksModel;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.utilities.FilesUtils;

import java.io.File;

public class ReadLinesTask implements Runnable {
    private final File file;
    private final TasksModel model;

    public ReadLinesTask(File file, TasksModel model) {
        this.file = file;
        this.model = model;
    }

    @Override
    public void run() {
        final long fileLength = FilesUtils.countLines(file);
        this.model.getExecutorService().submit(new DataManagerTask(new FileInfo(this.file, fileLength), this.model));
    }
}
