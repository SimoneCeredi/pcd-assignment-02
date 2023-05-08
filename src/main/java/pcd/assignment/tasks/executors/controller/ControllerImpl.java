package pcd.assignment.tasks.executors.controller;

import pcd.assignment.tasks.executors.model.Model;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFiles;
import pcd.assignment.utilities.Pair;
import pcd.assignment.view.View;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ControllerImpl implements Controller {

    private final Model model;
    private final View consoleView;
    private final View guiView;

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
        var ret = this.model.analyzeSources(directory);
        var results = ret.getX();
        var future = ret.getY();

        var worker = getSwingWorker(results, future);

        worker.execute();
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
        this.model.stop();
    }

    private SwingWorker<Void, Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> getSwingWorker(
            BlockingQueue<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> results,
            CompletableFuture<Void> future
    ) {
        return new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {
                while (!results.isEmpty() || !future.isDone()) {
                    Pair<UnmodifiableIntervals, UnmodifiableLongestFiles> result;
                    while ((result = results.poll()) != null) {
                        if (results.isEmpty()) {
                            publish(result);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Pair<UnmodifiableIntervals, UnmodifiableLongestFiles>> chunks) {
                for (var result : chunks) {
                    try {
                        guiView.show(result);
                    } catch (OperationNotSupportedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
