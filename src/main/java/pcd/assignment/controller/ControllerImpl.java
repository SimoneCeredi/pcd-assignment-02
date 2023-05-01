package pcd.assignment.controller;

import pcd.assignment.tasks.executors.model.Model;
import pcd.assignment.view.View;

import javax.naming.OperationNotSupportedException;
import java.io.File;
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
            this.consoleView.show(this.model.getReport(directory).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startGui(File directory) throws OperationNotSupportedException {
        var results = this.model.analyzeSources(directory);
        while (!results.isEmpty()) {
            try {
                this.guiView.show(results.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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

    }

}
