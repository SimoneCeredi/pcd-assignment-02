package pcd.assignment.base.controller;

import pcd.assignment.base.model.Model;
import pcd.assignment.base.model.data.results.Result;
import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.base.view.ExecutionStatus;
import pcd.assignment.base.view.View;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.*;

/**
 * Controller implementation.
 * This class uses the Model to fetch the final/intermediate result/s and eventually updates the View.
 */
public class ControllerImpl implements Controller {

    private Model model;
    private final View consoleView;
    private final View guiView;

    public ControllerImpl(View consoleView, View guiView) {
        this.consoleView = consoleView;
        this.guiView = guiView;
    }

    /**
     * Only the final result is needed: call model's getReport().
     * @param model where the results are fetched, which varies according to the specific version.
     * @param directory where to start the computation.
     * @throws OperationNotSupportedException
     */
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

    /**
     * All the intermediate results must be shown to the GUI: start a single threaded executor
     * to get the results from the Model and update the View
     * @param model ""
     * @param directory ""
     */
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

    /**
     * Using ResultsData from Model's analyzeSources(), communicate with the Model
     * that computation must be stopped.
     */
    @Override
    public void stop() {
        this.model.getResultsData().stop();
        this.guiView.setExecutionStatus(ExecutionStatus.COMPLETED);
    }

    /**
     * Executor's loop which remains active until computation is ended or stopped.
     * @param resultsData got from Model's analyzeSources()
     */
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
