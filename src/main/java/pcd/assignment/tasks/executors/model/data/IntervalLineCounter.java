package pcd.assignment.tasks.executors.model.data;

public interface IntervalLineCounter extends UnmodifiableIntervalLineCounter {
    void store(FileInfo fileInfo);

    void storeAll(IntervalLineCounter lineCounter);

    int getIntervals();

    int getMaxLines();

    IntervalLineCounter getCopy();
}
