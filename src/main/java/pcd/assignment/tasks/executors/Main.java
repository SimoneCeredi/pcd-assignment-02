package pcd.assignment.tasks.executors;

import pcd.assignment.controller.Controller;
import pcd.assignment.controller.ControllerImpl;
import pcd.assignment.tasks.executors.model.ModelImpl;
import pcd.assignment.view.ConsoleViewImpl;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File directory = new File("./files");
        int ni = 5;
        int maxl = 1000;
        int n = 10;
        if (args.length == 4) {
            directory = new File(args[0]);
            ni = Integer.parseInt(args[1]);
            maxl = Integer.parseInt(args[2]);
            n = Integer.parseInt(args[3]);
        }
        Controller controller = new ControllerImpl(new ModelImpl(ni, maxl, n), new ConsoleViewImpl(), new ConsoleViewImpl());
        controller.startConsole(directory);

    }
}
