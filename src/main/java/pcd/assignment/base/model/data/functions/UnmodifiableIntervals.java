package pcd.assignment.base.model.data.functions;

import pcd.assignment.base.utils.Pair;

import java.util.Map;

/**
 * Represents an {@link Intervals} that cannot be modified
 */
public interface UnmodifiableIntervals {

    Map<Pair<Integer, Integer>, UnmodifiableCounter> get();

}
