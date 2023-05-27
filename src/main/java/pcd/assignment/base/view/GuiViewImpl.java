package pcd.assignment.base.view;

import pcd.assignment.base.controller.Controller;
import pcd.assignment.base.controller.ControllerImpl;
import pcd.assignment.base.model.*;
import pcd.assignment.base.model.configuration.Configuration;
import pcd.assignment.base.model.configuration.ConfigurationImpl;
import pcd.assignment.base.model.data.results.Result;
import pcd.assignment.base.model.data.results.FileInfo;
import pcd.assignment.base.analyzer.SourceAnalyzer;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Comparator;
import java.util.function.Function;

public class GuiViewImpl extends JFrame implements View {

    private JTextField dTextField;
    private JTextField niTextField;
    private JTextField maxlTextField;
    private JTextField nTextField;
    private JButton startButton;
    private JButton stopButton;
    private JList<String> longestFiles; // longestFiles
    private JList<String> linesCounters; // linesCounters
    private boolean initialized = false;
    private Controller controller;
    private File rootDirectory;

    @Override
    public void initialize(Function<Model, SourceAnalyzer> sourceAnalyzerFunction,
                           File directory) {
        this.rootDirectory = directory;
        generateGUI(sourceAnalyzerFunction);
        this.initialized = true;
    }

    @Override
    public void show(Result result) throws OperationNotSupportedException {
        if (!initialized) {
            throw new OperationNotSupportedException("Gui not yet initialized");
        }
        SwingUtilities.invokeLater(() -> {
            this.linesCounters.setListData(result.getIntervals().get().entrySet().stream()
                    .sorted(Comparator.comparingInt(e -> e.getKey().getX()))
                    .map(e ->
                            e.getValue().getValue() +
                                    " files in range [" +
                                    e.getKey().getX() +
                                    (e.getKey().getY() == Integer.MAX_VALUE ? "+" : ("," + e.getKey().getY())) +
                                    "]"
                    )
                    .toArray(String[]::new)
            );
            this.longestFiles.setListData(
                    result.getLongestFiles().get().stream()
                            .sorted(Comparator.comparingLong(FileInfo::getNumberOfLines))
                            .map(f -> f.getFile().getName() + " -> " + f.getNumberOfLines())
                            .toArray(String[]::new)
            );
        });
    }

    @Override
    public void setExecutionStatus(ExecutionStatus status) {
        SwingUtilities.invokeLater(() -> {
            switch (status) {
                case STARTED -> {
                    this.startButton.setEnabled(false);
                    this.stopButton.setEnabled(true);
                }
                case COMPLETED -> {
                    this.startButton.setEnabled(true);
                    this.stopButton.setEnabled(false);
                }
            }
        });
    }

    private void generateGUI(Function<Model, SourceAnalyzer> sourceAnalyzerFunction) {
        // Set the title of the JFrame
        setTitle("My Java Swing View");

        // Set the size of the JFrame
        setSize(800, 600);

        // Set the layout manager for the JFrame
        setLayout(new BorderLayout());

        // Create a JPanel for the input boxes and labels
        JPanel inputBoxPanel = new JPanel(new BorderLayout());
        inputBoxPanel.setBorder(BorderFactory.createTitledBorder("Input"));

        // Create a JPanel for the folder selection
        JPanel folderPanel = new JPanel(new BorderLayout());
        JLabel dLabel = new JLabel("Select a folder:");
        dTextField = new JTextField(this.rootDirectory.getAbsolutePath());
        JButton dButton = new JButton("...");
        dButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(GuiViewImpl.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                this.rootDirectory = new File(fileChooser.getSelectedFile().getAbsolutePath());
                dTextField.setText(this.rootDirectory.getAbsolutePath());
            }
        });
        folderPanel.add(dLabel, BorderLayout.WEST);
        folderPanel.add(dTextField, BorderLayout.CENTER);
        folderPanel.add(dButton, BorderLayout.EAST);

        // Add the folder panel to the input panel
        inputBoxPanel.add(folderPanel, BorderLayout.NORTH);

        // Create a JPanel for the input boxes
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        // Create the input boxes and labels
        JLabel niLabel = new JLabel("NI:");
        niTextField = new JTextField();
        JLabel maxlLabel = new JLabel("MAXL:");
        maxlTextField = new JTextField();
        JLabel nLabel = new JLabel("N:");
        nTextField = new JTextField();

        // Add the input boxes and labels to the input panel
        inputPanel.add(niLabel);
        inputPanel.add(niTextField);
        inputPanel.add(maxlLabel);
        inputPanel.add(maxlTextField);
        inputPanel.add(nLabel);
        inputPanel.add(nTextField);

        // Add the input panel to the input panel
        inputBoxPanel.add(inputPanel, BorderLayout.CENTER);

        // Add the input panel to the JFrame
        add(inputBoxPanel, BorderLayout.CENTER);

        // Create a JPanel for the lists and buttons
        JPanel listPanel = new JPanel(new BorderLayout());

        // Create the left and right lists
        longestFiles = new JList<>();
        JScrollPane longestFilesScrollPane = new JScrollPane(longestFiles);
        longestFilesScrollPane.setBorder(BorderFactory.createTitledBorder("Longest Files"));
        listPanel.add(longestFilesScrollPane);

        linesCounters = new JList<>();
        JScrollPane linesCountersScrollPane = new JScrollPane(linesCounters);
        linesCountersScrollPane.setBorder(BorderFactory.createTitledBorder("Intervals"));
        listPanel.add(linesCountersScrollPane);

        // Create a JPanel for the lists
        JPanel lists = new JPanel(new GridLayout(1, 2));
        lists.setPreferredSize(new Dimension(600, 300)); // set size to 600x300 pixels
        lists.add(longestFilesScrollPane);
        lists.add(linesCountersScrollPane);
        listPanel.add(lists, BorderLayout.CENTER);

        // Create a JPanel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        // Create the Start and Stop buttons
        startButton = new JButton("Start");
        startButton.addActionListener(l -> Thread.ofVirtual().start(() -> {
            try {
                Configuration configuration;
                String ni = niTextField.getText();
                String maxl = maxlTextField.getText();
                String n = nTextField.getText();
                if (!ni.isEmpty() && !maxl.isEmpty() && !n.isEmpty()) {
                    configuration = new ConfigurationImpl(
                            Integer.parseInt(ni),
                            Integer.parseInt(maxl),
                            Integer.parseInt(n));
                } else {
                    configuration = new ConfigurationImpl();
                }
                Model model = new ModelBuilderImpl()
                        .setConfiguration(configuration)
                        .setSourceAnalyzer(sourceAnalyzerFunction)
                        .build();
                this.controller = new ControllerImpl(null, this);
                this.controller.start(model, this.rootDirectory);
            } catch (OperationNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }));
        stopButton = new JButton("Stop");
        stopButton.addActionListener(l -> Thread.ofVirtual().start(() -> this.controller.stop()));

        // Add the buttons to the button panel
        buttonPanel.add(stopButton);
        buttonPanel.add(startButton);
        listPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the list panel below the input boxes
        add(listPanel, BorderLayout.SOUTH);

        // Sets start and stop button beginning status
        this.startButton.setEnabled(true);
        this.stopButton.setEnabled(false);

        // Set the JFrame to be visible
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
