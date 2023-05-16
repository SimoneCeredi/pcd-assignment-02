package pcd.assignment.common.analyzer;

import pcd.assignment.common.model.data.results.ResultsData;
import pcd.assignment.common.model.data.results.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SourceAnalyzer {

    default CompletableFuture<Result> getReport(File directory) {
        ResultsData resultsData = this.analyzeSources(directory);
        CompletableFuture<Result> finalResultFuture = new CompletableFuture<>();
        resultsData
                .getCompletionFuture()
                .whenComplete((unused, throwable) ->
                        finalResultFuture.completeAsync(() -> {
                            List<Result> results = new ArrayList<>();
                            resultsData.getResults().drainTo(results);
                            return results.get(results.size() - 1);
                        })
                );
        return finalResultFuture;
    }

    ResultsData analyzeSources(File directory);

}
