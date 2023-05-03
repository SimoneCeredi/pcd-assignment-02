package pcd.assignment.view;

import pcd.assignment.controller.Controller;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.UnmodifiableIntervalLineCounter;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableLongestFilesQueue;
import pcd.assignment.utilities.Pair;

import java.io.File;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ConsoleViewImpl implements View {

    @Override
    public void initialize(Controller controller, File directory) {

    }

    @Override
    public void show(Pair<UnmodifiableIntervalLineCounter, UnmodifiableLongestFilesQueue> result) {
        System.out.println("Longest Files");
        System.out.println(result.getY().get().stream()
                .sorted(Comparator.comparingLong(FileInfo::getLineCount))
                .map(f -> f.getFile().getAbsolutePath() + " -> " + f.getLineCount())
                .collect(Collectors.joining("\n")));
        System.out.println("\n\nLines distribution");
        result.getX().get().entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().getX()))
                .forEach(e -> System.out.println(
                        e.getValue().getValue() +
                                " files in range [" +
                                e.getKey().getX() +
                                (e.getKey().getY() == Integer.MAX_VALUE ? "+" : ("," + e.getKey().getY())) +
                                "]"
                ));
        System.out.println("Total files -> " + result.getX().get().values().stream().mapToInt(UnmodifiableCounter::getValue).sum());

    }
}
