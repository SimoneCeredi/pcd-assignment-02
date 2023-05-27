package pcd.assignment.base.view;

import pcd.assignment.base.model.Model;
import pcd.assignment.base.model.data.results.Result;
import pcd.assignment.base.analyzer.SourceAnalyzer;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.function.Function;

public interface View {
    void initialize(Function<Model, SourceAnalyzer> sourceAnalyzerFunction,
                    File directory);

    void show(Result result) throws OperationNotSupportedException;

    void setExecutionStatus(ExecutionStatus status);
}
