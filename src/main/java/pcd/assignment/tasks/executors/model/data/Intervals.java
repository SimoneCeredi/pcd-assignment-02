package pcd.assignment.tasks.executors.model.data;

public interface Intervals extends UnmodifiableIntervals {
    void store(FileInfo fileInfo);

    void storeAll(Intervals lineCounter);

    int getIntervals();

    int getMaxLines();

    Intervals getCopy();
}
