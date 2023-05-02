package pcd.assignment.controller;

import pcd.assignment.tasks.executors.model.Model;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;
import pcd.assignment.view.View;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;

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
            this.consoleView.show(this.model.getReport(directory).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startGui(File directory) throws OperationNotSupportedException {
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

    private SwingWorker<Void, Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> getSwingWorker(
            BlockingQueue<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> results,
            ForkJoinTask<Pair<IntervalLineCounter, LongestFilesQueue>> future
    ) {
        return new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {
                while (!results.isEmpty() || !future.isDone()) {
                    Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>> result = results.take();
                    publish(result);
                }
                return null;
            }

            @Override
            protected void process(List<Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>>> chunks) {
                for (Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>> result : chunks) {
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
