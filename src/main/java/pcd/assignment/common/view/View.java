package pcd.assignment.common.view;

import pcd.assignment.common.controller.Controller;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.data.Result;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.utilities.Pair;
import pcd.assignment.common.model.data.UnmodifiableIntervals;
import pcd.assignment.common.model.data.monitor.UnmodifiableLongestFiles;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.function.Function;

public interface View {
    void initialize(Function<Model, SourceAnalyzer> sourceAnalyzerFunction,
                    File directory);

    void show(Result result) throws OperationNotSupportedException;

    void setExecutionStatus(ExecutionStatus status);
}
