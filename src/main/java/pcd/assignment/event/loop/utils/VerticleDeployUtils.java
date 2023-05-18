package pcd.assignment.event.loop.utils;

import io.vertx.core.DeploymentOptions;

public class VerticleDeployUtils {

    public static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
    }

    public static DeploymentOptions getDeploymentOptions() {
        return new DeploymentOptions().setWorker(true);
    }

}
