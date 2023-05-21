package pcd.assignment.event.loop.utils;

import io.vertx.core.DeploymentOptions;

/**
 * Event loop utils.
 */
public class VerticleDeployUtils {

    public static DeploymentOptions getDeploymentOptions() {
        return new DeploymentOptions().setWorker(true);
    }

}
