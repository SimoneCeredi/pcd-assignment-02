package pcd.assignment.tasks.executors.tasks;

import pcd.assignment.common.model.data.results.FileInfo;
import pcd.assignment.common.model.data.results.Result;
import pcd.assignment.common.model.data.results.ResultImpl;
import pcd.assignment.common.analyzer.SourceAnalyzerData;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ExploreDirectoryTask extends RecursiveTask<Result> {
    private final File directory;
    private final SourceAnalyzerData data;

    public ExploreDirectoryTask(File directory, SourceAnalyzerData data) {
        this.directory = directory;
        this.data = data;
    }


    @Override
    protected Result compute() {
        List<RecursiveTask<Result>> directoryForks = new LinkedList<>();
        List<RecursiveTask<FileInfo>> filesForks = new LinkedList<>();
        exploreAndFork(directoryForks, filesForks);
        joinDirectoriesTask(directoryForks);
        joinReadLinesTasks(filesForks);

        if (!this.data.getResultsData().isStopped()) {
            try {
                this.data
                        .getResultsData()
                        .getResults()
                        .put(new ResultImpl(this.data.getCurrentIntervals().getCopy(),
                                this.data.getCurrentLongestFiles().getCopy()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResultImpl(this.data.getCurrentIntervals(), this.data.getCurrentLongestFiles());
    }

    private void joinReadLinesTasks(List<RecursiveTask<FileInfo>> filesForks) {
        for (var task : filesForks) {
            FileInfo fileInfo = task.join();
            this.data.getCurrentIntervals().store(fileInfo);
            this.data.getCurrentLongestFiles().put(fileInfo);
        }
    }

    private void joinDirectoriesTask(List<RecursiveTask<Result>> directoryForks) {
        for (var task : directoryForks) {
            task.join();
        }
    }

    private void exploreAndFork(List<RecursiveTask<Result>> directoryForks,
                                List<RecursiveTask<FileInfo>> filesForks) {
        if (this.directory.isDirectory()) {
            File[] files = this.directory.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length && !this.data.getResultsData().isStopped(); i++) {
                    if (files[i].isDirectory()) {
                        ExploreDirectoryTask task = getExploreDirectoryTask(files[i]);
                        directoryForks.add(task);
                        task.fork();
                    } else {
                        if (files[i].getName().endsWith(".java")) {
                            ReadLinesTask task = new ReadLinesTask(files[i]);
                            filesForks.add(task);
                            task.fork();
                        }
                    }
                }
            }
        }
    }

    private ExploreDirectoryTask getExploreDirectoryTask(File file) {
        return new ExploreDirectoryTask(file, this.data);
    }
}