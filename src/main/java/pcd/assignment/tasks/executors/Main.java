package pcd.assignment.tasks.executors;

import pcd.assignment.tasks.executors.model.Model;
import pcd.assignment.tasks.executors.model.ModelImpl;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounterImpl;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueueImpl;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import java.io.File;

public class Main {

    public static final File DIRECTORY = new File("./files");

    public static void main(String[] args) {
        System.out.println("1) Async approach with Tasks - Executors Framework");
        Model model = new ModelImpl(new IntervalLineCounterImpl(10, 1000), new LongestFilesQueueImpl(10));
        Pair<IntervalLineCounter, LongestFilesQueue> res;

        long startTime = System.currentTimeMillis();

//        res = model.getReport(DIRECTORY);

        long elapsedTime = System.currentTimeMillis() - startTime;

//        printRes(res);
        System.out.println("Elapsed time " + elapsedTime + "ms");

//        startTime = System.currentTimeMillis();
        var results = model.analyzeSources(DIRECTORY);
        while (!results.isEmpty()) {
            try {
                printRes(results.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static void printRes(Pair<IntervalLineCounter, LongestFilesQueue> res) {
//        System.out.println("\n\n\n\nLongest Files");
//        System.out.println(res.getY().get().stream()
//                .sorted(Comparator.comparingLong(FileInfo::getLineCount))
//                .map(f -> f.getFile().getAbsolutePath() + " -> " + f.getLineCount())
//                .collect(Collectors.joining("\n")));
//        System.out.println("\n\nLines distribution");
//        res.getX().get().entrySet().stream()
//                .sorted(Comparator.comparingInt(e -> e.getKey().getX()))
//                .forEach(e -> System.out.println(
//                        e.getValue().getValue() +
//                                " files in range [" +
//                                e.getKey().getX() +
//                                (e.getKey().getY() == Integer.MAX_VALUE ? "+" : ("," + e.getKey().getY())) +
//                                "]"
//                ));
        System.out.println("Total files -> " + (Integer) res.getX().get().values().stream().mapToInt(UnmodifiableCounter::getValue).sum());
    }
}
