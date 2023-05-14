package pcd.assignment.reactive.utils;

public class Log {

    public static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
    }

}
