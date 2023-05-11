package pcd.assignment.common.view;

import pcd.assignment.common.controller.Controller;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.UnmodifiableIntervals;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableLongestFiles;

import javax.naming.OperationNotSupportedException;
import java.io.File;

public interface View {
    void initialize(Controller controller, File directory);

    void show(Pair<UnmodifiableIntervals, UnmodifiableLongestFiles> result) throws OperationNotSupportedException;

    void setExecutionStatus(ExecutionStatus status);
}
