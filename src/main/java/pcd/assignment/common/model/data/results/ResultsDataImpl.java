package pcd.assignment.common.model.data.results;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A ResultsData implementation.
 */
public class ResultsDataImpl implements ResultsData {

    private final BlockingQueue<Result> results;
    private final CompletableFuture<Void> completionFuture;
    private volatile boolean stop;

    public ResultsDataImpl(BlockingQueue<Result> results,
                           CompletableFuture<Void> completionFuture) {
        this.results = results;
        this.completionFuture = completionFuture;
        this.stop = false;
    }

    public ResultsDataImpl() {
        this(new LinkedBlockingQueue<>(), new CompletableFuture<>());
    }

    @Override
    public BlockingQueue<Result> getResults() {
        return this.results;
    }

    @Override
    public CompletableFuture<Void> getCompletionFuture() {
        return this.completionFuture;
    }

    @Override
    public void stop() {
        this.stop = true;
    }

    @Override
    public boolean isStopped() {
        return this.stop;
    }

}
