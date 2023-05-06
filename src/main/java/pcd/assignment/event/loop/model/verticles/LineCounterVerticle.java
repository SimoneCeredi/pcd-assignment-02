package pcd.assignment.event.loop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class LineCounterVerticle extends AbstractVerticle {

    @Override
    public void start() {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("process-file", message -> {
            String file = (String) message.body();
            vertx.fileSystem().readFile(file, res -> {
                if (res.succeeded()) {
                    JsonObject fileInfo = new JsonObject()
                            .put("file-path", file)
                            .put("lines", res.result().toString().split("\\r?\\n").length);
                    eventBus.send(
                            "file-info",
                            fileInfo
                    );
                } else {
                    System.err.println("Failed to read the file " + res.cause().getMessage());
                }
            });
        });
    }
}
