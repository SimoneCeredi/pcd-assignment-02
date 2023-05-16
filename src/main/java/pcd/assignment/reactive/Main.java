package pcd.assignment.reactive;

import pcd.assignment.common.controller.ControllerImpl;
import pcd.assignment.common.model.configuration.ConfigurationImpl;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.ModelImpl;
import pcd.assignment.common.analyzer.SourceAnalyzer;
import pcd.assignment.common.view.ConsoleViewImpl;
import pcd.assignment.common.view.GuiViewImpl;
import pcd.assignment.common.view.View;
import pcd.assignment.reactive.analyzer.SourceAnalyzerImpl;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) throws OperationNotSupportedException {
        System.out.println("4) Reactive based approach");
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
                    .startConsole(model, directory);
        } else {
            File directory = new File("./benchmarks/fs");
            gui.initialize(sourceAnalyzerFunction, directory);
        }
    }

}
