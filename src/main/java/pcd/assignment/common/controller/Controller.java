package pcd.assignment.common.controller;

import pcd.assignment.common.model.Model;

import javax.naming.OperationNotSupportedException;
import java.io.File;

public interface Controller {

    void startConsole(Model model, File directory) throws OperationNotSupportedException;

    void start(Model model, File directory) throws OperationNotSupportedException;

    void stop();

}
