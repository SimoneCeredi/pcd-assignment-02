package pcd.assignment.controller;

import javax.naming.OperationNotSupportedException;
import java.io.File;

public interface Controller {
    void startConsole(File directory) throws OperationNotSupportedException;

    void startGui(File directory) throws OperationNotSupportedException;

    int getNi();

    void setNi(int ni);

    int getMaxl();

    void setMaxl(int maxl);

    int getN();

    void setN(int n);


    void stop();

}
