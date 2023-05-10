package pcd.assignment.common.model;

import pcd.assignment.common.source.analyzer.SourceAnalyzer;

public interface Model extends SourceAnalyzer {

    void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer);

    int getNi();

    void setNi(int ni);

    int getMaxl();

    void setMaxl(int maxl);

    int getN();

    void setN(int n);

}
