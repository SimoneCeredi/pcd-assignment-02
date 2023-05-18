package pcd.assignment.common.controller;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.results.Result;
import pcd.assignment.common.model.data.results.ResultsData;
import pcd.assignment.common.view.ExecutionStatus;
import pcd.assignment.common.view.View;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.*;

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
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> this.analyzeResults(resultsData));
        resultsData.getCompletionFuture().whenComplete((unused, throwable) -> {
            this.guiView.setExecutionStatus(ExecutionStatus.COMPLETED);
        });
    }


    @Override
    public void stop() {
        this.model.getResultsData().stop();
        this.guiView.setExecutionStatus(ExecutionStatus.COMPLETED);
    }

    private void analyzeResults(ResultsData resultsData) {
        Result result;
        while ((!resultsData.getResults().isEmpty() ||
                !resultsData.getCompletionFuture().isDone()) && !resultsData.isStopped()) {
            while ((result = resultsData.getResults().poll()) != null) {
                if (resultsData.getResults().isEmpty()) {
                    try {
                        this.guiView.show(result);
                    } catch (OperationNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
