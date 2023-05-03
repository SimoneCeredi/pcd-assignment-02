package pcd.assignment.tasks.executors.model.data;

import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.util.Map;

public interface UnmodifiableIntervalLineCounter {
    Map<Pair<Integer, Integer>, UnmodifiableCounter> get();
    
}
