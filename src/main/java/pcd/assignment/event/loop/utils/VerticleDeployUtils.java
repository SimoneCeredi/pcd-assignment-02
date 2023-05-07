package pcd.assignment.event.loop.utils;

import io.vertx.core.AsyncResult;

import java.util.concurrent.BlockingQueue;

public class VerticleDeployUtils {
    public static void evaluateDeployment(AsyncResult<String> ar, BlockingQueue<String> deployedVerticles) {
        if (ar.succeeded()) {
            deployedVerticles.add(ar.result());
        } else {
            System.err.println("Verticle deployment failed");
        }
    }
}
