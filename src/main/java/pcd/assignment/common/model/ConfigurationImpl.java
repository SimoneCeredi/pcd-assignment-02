package pcd.assignment.common.model;

public class ConfigurationImpl implements Configuration {
    private int numberOfIntervals;
    private int maximumLines;
    private int maximumFiles;

    protected ConfigurationImpl(int numberOfIntervals, int maximumLines, int maximumFiles) {
        this.numberOfIntervals = numberOfIntervals;
        this.maximumLines = maximumLines;
        this.maximumFiles = maximumFiles;
    }

    @Override
    public int getNumberOfIntervals() {
        return numberOfIntervals;
    }

    @Override
    public void setNumberOfIntervals(int numberOfIntervals) {
        this.numberOfIntervals = numberOfIntervals;
    }

    @Override
    public int getMaximumLines() {
        return maximumLines;
    }

    @Override
    public void setMaximumLines(int maximumLines) {
        this.maximumLines = maximumLines;
    }

    @Override
    public int getAtMostNFiles() {
        return maximumFiles;
    }

    @Override
    public void setAtMostNFiles(int maximumNFiles) {
        this.maximumFiles = maximumNFiles;
    }

}
