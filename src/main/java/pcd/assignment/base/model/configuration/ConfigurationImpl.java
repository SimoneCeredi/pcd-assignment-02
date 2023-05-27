package pcd.assignment.base.model.configuration;

/**
 * A Configuration implementation.
 */
public class ConfigurationImpl implements Configuration {

    private static final int DEFAULT_NI = 5;
    private static final int DEFAULT_MAXL = 1_000;
    private static final int DEFAULT_N = 10;

    private final int numberOfIntervals;
    private final int maximumLines;
    private final int maximumFiles;

    public ConfigurationImpl(int numberOfIntervals, int maximumLines, int maximumFiles) {
        this.numberOfIntervals = numberOfIntervals;
        this.maximumLines = maximumLines;
        this.maximumFiles = maximumFiles;
    }

    public ConfigurationImpl() {
        this(DEFAULT_NI, DEFAULT_MAXL, DEFAULT_N);
    }

    @Override
    public int getNumberOfIntervals() {
        return numberOfIntervals;
    }

    @Override
    public int getMaximumLines() {
        return maximumLines;
    }

    @Override
    public int getAtMostNFiles() {
        return maximumFiles;
    }

}
