package pcd.assignment.impl.eventloop.model.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import pcd.assignment.base.model.data.results.FileInfo;
import pcd.assignment.base.model.data.results.ResultImpl;
import pcd.assignment.base.analyzer.SourceAnalyzerData;
import pcd.assignment.base.utilities.FilesUtils;

import java.io.File;

/**
 * Read lines as an AbstractVerticle.
 */
public class ReadLinesVerticle extends AbstractVerticle {

    private final String file;
    private final Promise<Void> promise;
    private final SourceAnalyzerData data;

    public ReadLinesVerticle(String file, Promise<Void> promise, SourceAnalyzerData data) {
        this.file = file;
        this.promise = promise;
        this.data = data;
    }

    /**
     * Open the file and read the number of lines it contains.
     * In the end, complete the promise.
     */
    @Override
    public void start() {
        vertx.fileSystem().readFile(this.file, res -> {
            if (res.succeeded()) {
                if (!this.data.getResultsData().isStopped()) {
                    File ffile = new File(file);
                    final long fileLength = FilesUtils.countLines(ffile);
                    saveFileInfo(new FileInfo(ffile, fileLength));
                }
                this.promise.complete();
            } else {
                System.err.println("Failed to read the file " + res.cause().getMessage());
                this.promise.fail(res.cause().getMessage());
            }
        });
    }

    /**
     * Save file information onto SourceAnalyzerData
     * @param fileInfo
     */
    private void saveFileInfo(FileInfo fileInfo) {
        this.data.getCurrentIntervals().store(fileInfo);
        this.data.getCurrentLongestFiles().put(fileInfo);
        try {
            this.data.getResultsData().getResults().put(
                    new ResultImpl(
                            this.data.getCurrentIntervals().getCopy(),
                            this.data.getCurrentLongestFiles().getCopy()
                    )
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
