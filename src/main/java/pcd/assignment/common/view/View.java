package pcd.assignment.common.view;

import pcd.assignment.common.controller.Controller;
import pcd.assignment.common.model.data.Result;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;

import javax.naming.OperationNotSupportedException;
import java.io.File;

public interface View {
    void initialize(Controller controller, File directory);

    void show(Result result) throws OperationNotSupportedException;

    void setExecutionStatus(ExecutionStatus status);
}
