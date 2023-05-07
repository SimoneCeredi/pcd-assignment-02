package pcd.assignment.event.loop;

import pcd.assignment.event.loop.model.ModelImpl;
import pcd.assignment.tasks.executors.controller.Controller;
import pcd.assignment.tasks.executors.controller.ControllerImpl;
import pcd.assignment.view.ConsoleViewImpl;
import pcd.assignment.view.GuiViewImpl;
import pcd.assignment.view.View;

import javax.naming.OperationNotSupportedException;
import java.io.File;

public class Main {
    public static void main(String[] args) throws OperationNotSupportedException {
        System.out.println("3) Async approach event based");
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
        View gui = new GuiViewImpl();
        Controller controller = new ControllerImpl(new ModelImpl(ni, maxl, n), new ConsoleViewImpl(), gui);
        controller.startConsole(directory);
        gui.initialize(controller, directory);
    }

}
