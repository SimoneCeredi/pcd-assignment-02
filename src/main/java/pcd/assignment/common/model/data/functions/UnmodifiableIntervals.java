package pcd.assignment.common.model.data.functions;

import pcd.assignment.common.model.data.functions.Intervals;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.common.model.data.functions.UnmodifiableCounter;

import java.util.Map;

/**
 * Represents an {@link Intervals} that cannot be modified
 */
public interface UnmodifiableIntervals {
    Map<Pair<Integer, Integer>, UnmodifiableCounter> get();

}
