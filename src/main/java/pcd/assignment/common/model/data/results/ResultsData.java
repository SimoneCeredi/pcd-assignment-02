package pcd.assignment.common.model.data.results;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

/**
 * ResultsData models the communication between the Controller and the Model.
 */
public interface ResultsData {

    /**
     * The Result(s) computed by the Model are inserted in the returned BlockingQueue
     * which could be read interactively by the Controller.
     * @return blocking queue of partial Result(s).
     */
    BlockingQueue<Result> getResults();

    /**
     * @return A completable future which is completed by the Model when computation is ended.
     */
    CompletableFuture<Void> getCompletionFuture();

    /**
     * The Controller calls this method when computation must be stopped.
     */
    void stop();

    /**
     * Used by the Model to check if computation must be stopped.
     * @return true if it's stopped, false otherwise.
     */
    boolean isStopped();

}