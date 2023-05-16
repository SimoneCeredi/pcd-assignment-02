package pcd.assignment.common.model.data.results;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public interface ResultsData {

    BlockingQueue<Result> getResults();

    CompletableFuture<Void> getCompletionFuture();

    void stop();

    boolean isStopped();

}