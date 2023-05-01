package pcd.assignment.controller;

import java.io.File;

public interface Controller {
    void startConsole(File directory);

    void startGui(File directory);

    void setNi(int ni);

    void setMaxl(int maxl);

    void setN(int n);


    void stop();

}
