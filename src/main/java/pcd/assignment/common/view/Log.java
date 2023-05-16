package pcd.assignment.common.view;

public class Log {

    public static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
    }

}
