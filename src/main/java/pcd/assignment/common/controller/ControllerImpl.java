package pcd.assignment.common.controller;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.common.view.ExecutionStatus;
import pcd.assignment.common.view.View;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.concurrent.*;

public class ControllerImpl implements Controller {

    private final Model model;
    private final View consoleView;
    private final View guiView;
    private ExecutorService executorService;
    private volatile boolean stop;

    public ControllerImpl(Model model, View consoleView, View guiView) {
        this.model = model;
        this.consoleView = consoleView;
        this.guiView = guiView;
    }

    @Override
    public void startConsole(File directory) throws OperationNotSupportedException {
        try {
            long startTime = System.currentTimeMillis();
            this.consoleView.show(this.model.getReport(directory).get());
            System.out.println("Execution completed in -> " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startGui(File directory) {
        this.stop = false;
        this.guiView.setExecutionStatus(ExecutionStatus.STARTED);
        var ret = this.model.analyzeSources(directory);
        var results = ret.getX();
        var future = ret.getY();

        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.submit(() -> this.analyzeResults(results, future));

        future.whenComplete((unused, throwable) -> {
            this.guiView.setExecutionStatus(ExecutionStatus.COMPLETED);
        });
    }


    @Override
    public int getNi() {
        return this.model.getNi();
    }

    @Override
    public void setNi(int ni) {
        this.model.setNi(ni);

    }

    @Override
    public int getMaxl() {
        return this.model.getMaxl();
    }

    @Override
    public void setMaxl(int maxl) {
        this.model.setMaxl(maxl);

    }

    @Override
    public int getN() {
        return this.model.getN();
    }

    @Override
    public void setN(int n) {
        this.model.setN(n);

    }

    @Override
    public void stop() {
        this.stop = true;
        this.executorService.shutdownNow();
        this.model.stop();
        this.guiView.setExecutionStatus(ExecutionStatus.COMPLETED);
    }

    private void analyzeResults(
            BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results,
            CompletableFuture<Void> future
    ) {
        while ((!results.isEmpty() || !future.isDone()) && !stop) {
            Pair<UnmodifiableIntervals, UnmodifiableLongestFiles> result;
            while ((result = results.poll()) != null) {
                if (results.isEmpty()) {
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
