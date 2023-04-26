package pcd.assignment.tasks.executors.model.tasks;

import pcd.assignment.tasks.executors.model.TasksModel;

import java.io.File;

public class ExploreDirectoryTask implements Runnable {
    private final File directory;
    private final TasksModel model;

    public ExploreDirectoryTask(File directory, TasksModel model) {
        this.directory = directory;
        this.model = model;
    }

    @Override
    public void run() {
        if (this.directory.isDirectory()) {
            File[] files = this.directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        this.model.getExecutorService().submit(new ExploreDirectoryTask(file, this.model));
                    } else {
                        if (file.getName().endsWith(".java")) {
                            this.model.getExecutorService().submit(new ReadLinesTask(file, this.model));
                        }
                    }
                }
            }
        }
    }
}
