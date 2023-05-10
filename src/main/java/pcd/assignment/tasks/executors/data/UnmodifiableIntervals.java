package pcd.assignment.tasks.executors.data;

import pcd.assignment.common.utilities.Pair;
import pcd.assignment.tasks.executors.data.monitor.UnmodifiableCounter;

import java.util.Map;

/**
 * Represents an {@link Intervals} that cannot be modified
 */
public interface UnmodifiableIntervals {
    Map<Pair<Integer, Integer>, UnmodifiableCounter> get();

}
