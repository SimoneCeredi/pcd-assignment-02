package pcd.assignment.impl.taskexecutor.tasks;

import pcd.assignment.base.model.data.results.FileInfo;
import pcd.assignment.base.model.data.results.Result;
import pcd.assignment.base.model.data.results.ResultImpl;
import pcd.assignment.base.analyzer.SourceAnalyzerData;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Explore directory task.
 */
public class ExploreDirectoryTask extends RecursiveTask<Result> {
    private final File directory;
    private final SourceAnalyzerData data;

    public ExploreDirectoryTask(File directory, SourceAnalyzerData data) {
        this.directory = directory;
        this.data = data;
    }

    /**
     * Computes the result of the current directory waiting the completion of its children.
     * @return the Result
     */
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

    /**
     * Join ReadLinesTasks
     * @param filesForks
     */
    private void joinReadLinesTasks(List<RecursiveTask<FileInfo>> filesForks) {
        for (var task : filesForks) {
            FileInfo fileInfo = task.join();
            this.data.getCurrentIntervals().store(fileInfo);
            this.data.getCurrentLongestFiles().put(fileInfo);
        }
    }

    /**
     * Join ExploreDirectoryTasks
     * @param directoryForks
     */
    private void joinDirectoriesTask(List<RecursiveTask<Result>> directoryForks) {
        for (var task : directoryForks) {
            task.join();
        }
    }

    /**
     * List the content of this.directory:
     *  - Fork a ExploreDirectoryTask for each directory
     *  - Fork a ReadLinesTask for each file
     * @param directoryForks
     * @param filesForks
     */
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

    /**
     * Get a new ExploreDirectoryTask from the current data
     * @param file
     * @return ExploreDirectoryTask
     */
    private ExploreDirectoryTask getExploreDirectoryTask(File file) {
        return new ExploreDirectoryTask(file, this.data);
    }
}