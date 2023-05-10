package pcd.assignment.tasks.executors.data;

/**
 * Stores the intervals
 */
public interface Intervals extends UnmodifiableIntervals {
    void store(FileInfo fileInfo);

    void storeAll(Intervals lineCounter);

    int getIntervals();

    int getMaxLines();

    Intervals getCopy();
}
