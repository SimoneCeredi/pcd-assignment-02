package pcd.assignment.view;

import pcd.assignment.tasks.executors.controller.Controller;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.utilities.Pair;

import javax.naming.OperationNotSupportedException;
import java.io.File;

public interface View {
    void initialize(Controller controller, File directory);


    void show(Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue> result) throws OperationNotSupportedException;
}
