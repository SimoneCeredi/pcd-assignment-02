package pcd.assignment.base.analyzer;

import pcd.assignment.base.model.data.results.ResultsData;
import pcd.assignment.base.model.data.results.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * SourceAnalyzer interface
 */
public interface SourceAnalyzer {

    /**
     * Default getReport based on analyzeSources.
     * When the computation is ended complete the finalResultFuture with the last Result.
     * @param directory where to start the computation
     * @return A completable future which completes once the final result is ready.
     */
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
