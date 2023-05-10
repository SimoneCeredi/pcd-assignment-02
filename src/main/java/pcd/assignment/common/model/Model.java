package pcd.assignment.common.model;

import pcd.assignment.common.source.analyzer.SourceAnalyzer;

public interface Model extends BaseModel, SourceAnalyzer {

    void setSourceAnalyzer(SourceAnalyzer sourceAnalyzer);
}
