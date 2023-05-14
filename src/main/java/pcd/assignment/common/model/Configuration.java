package pcd.assignment.common.model;

public interface Configuration {
    int getNumberOfIntervals();

    void setNumberOfIntervals(int ni);

    int getMaximumLines();

    void setMaximumLines(int maximumLines);

    int getAtMostNFiles();

    void setAtMostNFiles(int maximumNFiles);
}
