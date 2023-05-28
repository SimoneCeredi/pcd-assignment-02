package pcd.assignment.impl.virtualthreads;

import pcd.assignment.base.controller.ControllerImpl;
import pcd.assignment.base.model.configuration.ConfigurationImpl;
import pcd.assignment.base.model.Model;
import pcd.assignment.base.model.ModelImpl;
import pcd.assignment.base.analyzer.SourceAnalyzer;
import pcd.assignment.base.utils.FilesUtils;
import pcd.assignment.base.view.ConsoleViewImpl;
import pcd.assignment.base.view.GuiViewImpl;
import pcd.assignment.base.view.View;
import pcd.assignment.impl.virtualthreads.analyzer.VirtualThreadsSourceAnalyzer;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.function.Function;

public class VirtualThreadsMain {

    public static void main(String[] args) throws OperationNotSupportedException {
        System.out.println("Virtual threads-based approach");
        Function<Model, SourceAnalyzer> sourceAnalyzerFunction = VirtualThreadsSourceAnalyzer::new;
        View gui = new GuiViewImpl();
        if (args.length == 4) {
            File directory = new File(args[0]);
            int ni = Integer.parseInt(args[1]);
            int maxl = Integer.parseInt(args[2]);
            int n = Integer.parseInt(args[3]);
            Model model = new ModelImpl(
                    new ConfigurationImpl(ni, maxl, n));
            model.setSourceAnalyzer(sourceAnalyzerFunction.apply(model));
            new ControllerImpl(new ConsoleViewImpl(), gui)
                    .startConsole(model, directory);
        } else {
            File directory = new File(FilesUtils.DEFAULT_FS_PATH);
            gui.initialize(sourceAnalyzerFunction, directory);
        }
    }

}
