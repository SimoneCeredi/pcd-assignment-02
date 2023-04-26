package pcd.assignment.tasks.executors;

import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        System.out.println("1) Async approach with Tasks - Executors Framework");

        ForkJoinPool executor = new ForkJoinPool();
        executor.execute(() -> System.out.println("Nice"));


    }
}
