package pcd.assignment.reactive;

import pcd.assignment.common.controller.Controller;
import pcd.assignment.common.controller.ControllerImpl;
import pcd.assignment.common.model.Model;
import pcd.assignment.common.model.ModelBuilderImpl;
import pcd.assignment.common.view.ConsoleViewImpl;
import pcd.assignment.common.view.GuiViewImpl;
import pcd.assignment.common.view.View;
import pcd.assignment.reactive.source.analyzer.SourceAnalyzerImpl;

import javax.naming.OperationNotSupportedException;
import java.io.File;

public class Main {
    public static void main(String[] args) throws OperationNotSupportedException {
        File directory = new File("./benchmarks/fs");
        int ni = 5;
        int maxl = 1000;
        int n = 10;
        if (args.length == 4) {
            directory = new File(args[0]);
            ni = Integer.parseInt(args[1]);
            maxl = Integer.parseInt(args[2]);
            n = Integer.parseInt(args[3]);
        }
        Model model = new ModelBuilderImpl()
                .setNi(ni)
                .setMaxl(maxl)
                .setN(n)
                .setSourceAnalyzer(SourceAnalyzerImpl::new)
                .build();
        View gui = new GuiViewImpl();
        Controller controller = new ControllerImpl(model, new ConsoleViewImpl(), gui);
        //controller.startConsole(directory);
        gui.initialize(controller, directory);
    }
}
