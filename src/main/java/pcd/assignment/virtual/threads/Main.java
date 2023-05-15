package pcd.assignment.virtual.threads;

import pcd.assignment.common.controller.ControllerImpl;
import pcd.assignment.common.model.ConfigurationImpl;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.ModelImpl;
import pcd.assignment.common.source.analyzer.SourceAnalyzer;
import pcd.assignment.common.view.ConsoleViewImpl;
import pcd.assignment.common.view.GuiViewImpl;
import pcd.assignment.common.view.View;
import pcd.assignment.virtual.threads.source.analyzer.SourceAnalyzerImpl;
import java.io.File;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        System.out.println("2) Virtual Threads based approach");
        Function<Model, SourceAnalyzer> sourceAnalyzerFunction = SourceAnalyzerImpl::new;
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
                    .start(model, directory);
        } else {
            File directory = new File("./benchmarks/fs");
            gui.initialize(sourceAnalyzerFunction, directory);
        }
    }

}
