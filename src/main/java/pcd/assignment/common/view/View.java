package pcd.assignment.common.view;

import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.results.Result;
import pcd.assignment.common.analyzer.SourceAnalyzer;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.function.Function;

public interface View {
    void initialize(Function<Model, SourceAnalyzer> sourceAnalyzerFunction,
                    File directory);

    void show(Result result) throws OperationNotSupportedException;

    void setExecutionStatus(ExecutionStatus status);
}
