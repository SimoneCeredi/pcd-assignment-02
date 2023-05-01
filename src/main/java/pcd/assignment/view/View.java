package pcd.assignment.view;

import pcd.assignment.controller.Controller;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.util.Collection;
import java.util.Map;

public interface View {
    void initialize(Controller controller);


    void show(Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>> result);
}
