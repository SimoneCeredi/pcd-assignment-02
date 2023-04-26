package pcd.assignment.tasks.executors;

import pcd.assignment.tasks.executors.model.Model;
import pcd.assignment.tasks.executors.model.ModelImpl;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.IntervalLineCounterImpl;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueue;
import pcd.assignment.tasks.executors.model.data.monitor.LongestFilesQueueImpl;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("1) Async approach with Tasks - Executors Framework");
        Model model = new ModelImpl(new IntervalLineCounterImpl(10, 1000), new LongestFilesQueueImpl(10));
        Pair<IntervalLineCounter, LongestFilesQueue> res;
        try {
            res = model.getReport(new File("./files")).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n\n\n\nLongest Files");
        System.out.println(res.getY().get().stream()
                .sorted(Comparator.comparingLong(FileInfo::getLineCount))
                .map(f -> f.getFile().getAbsolutePath() + " -> " + f.getLineCount())
                .collect(Collectors.joining("\n")));
        System.out.println("\n\nLines distribution");
        res.getX().get().entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().getX()))
                .forEach(e -> System.out.println(
                        e.getValue().getValue() +
                                " files in range [" +
                                e.getKey().getX() +
                                (e.getKey().getY() == Integer.MAX_VALUE ? "+" : ("," + e.getKey().getY())) +
                                "]"
                ));

    }
}
