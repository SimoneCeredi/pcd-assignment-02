package pcd.assignment.common.controller;

import pcd.assignment.common.model.Model;

import javax.naming.OperationNotSupportedException;
import java.io.File;

/**
 * Controller interface
 */
public interface Controller {

    /**
     * This method is called by the Main when the final result only is needed.
     * No GUI will be spawned
     * @param model where the results are fetched, which varies according to the specific version.
     * @param directory where to start the computation.
     * @throws OperationNotSupportedException
     */
    void startConsole(Model model, File directory) throws OperationNotSupportedException;

    /**
     * This method is called by the View when also intermediate results must be shown.
     * @param model ""
     * @param directory ""
     * @throws OperationNotSupportedException
     */
    void start(Model model, File directory) throws OperationNotSupportedException;

    /**
     * Called by the View after a start() when the computation must be stopped.
     */
    void stop();

}
