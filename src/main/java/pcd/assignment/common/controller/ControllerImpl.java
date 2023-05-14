package pcd.assignment.common.controller;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.Result;
import pcd.assignment.common.model.data.ResultsData;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.common.view.ExecutionStatus;
import pcd.assignment.common.view.View;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

public class ControllerImpl implements Controller {

    private Model model;
    private final View consoleView;
    private final View guiView;

    public ControllerImpl(View consoleView, View guiView) {
        this.consoleView = consoleView;
        this.guiView = guiView;
    }

    @Override
    public void startConsole(Model model, File directory) throws OperationNotSupportedException {
        this.model = model;
        try {
            long startTime = System.currentTimeMillis();
            this.consoleView.show(this.model.getReport(directory).get());
            System.out.println("Execution completed in -> " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Model model, File directory) {
        this.model = model;
        this.guiView.setExecutionStatus(ExecutionStatus.STARTED);
        ResultsData resultsData = this.model.analyzeSources(directory);
        var worker = getSwingWorker(resultsData);
        resultsData.getCompletionFuture()
                .whenComplete((unused, throwable) ->
                        this.guiView.setExecutionStatus(ExecutionStatus.COMPLETED));
        worker.execute();
    }

    @Override
    public void stop() {
        this.model.getResultsData().stop();
        this.guiView.setExecutionStatus(ExecutionStatus.COMPLETED);
    }

    private SwingWorker<Void, Result> getSwingWorker(ResultsData resultsData) {
        return new SwingWorker<>() {

            @Override
            protected Void doInBackground() {
                BlockingQueue<Result> results = resultsData.getResults();
                Result result;
                while ((!results.isEmpty() || !resultsData.getCompletionFuture().isDone()) &&
                        !resultsData.isStopped()) {
                    while ((result = results.poll()) != null) {
                        if (results.isEmpty()) {
                            publish(result);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Result> chunks) {
                for (var result : chunks) {
                    try {
                        guiView.show(result);
                    } catch (OperationNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        };
    }

}
