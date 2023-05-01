package pcd.assignment.view;

import pcd.assignment.controller.Controller;
import pcd.assignment.tasks.executors.model.data.FileInfo;
import pcd.assignment.tasks.executors.model.data.monitor.UnmodifiableCounter;
import pcd.assignment.utilities.Pair;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

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
    private File d;
    private int ni;
    private int maxl;
    private int n;

    @Override
    public void initialize(Controller controller, File directory) {
        this.controller = controller;
        this.d = directory;
        this.ni = this.controller.getNi();
        this.maxl = this.controller.getMaxl();
        this.n = this.controller.getN();
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
        dTextField = new JTextField(this.d.getAbsolutePath());
        JButton dButton = new JButton("...");
        dButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(GuiViewImpl.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                this.d = new File(fileChooser.getSelectedFile().getAbsolutePath());
                dTextField.setText(this.d.getAbsolutePath());
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
        niTextField = new JTextField(String.valueOf(this.ni));
        JLabel maxlLabel = new JLabel("MAXL:");
        maxlTextField = new JTextField(String.valueOf(this.maxl));
        JLabel nLabel = new JLabel("N:");
        nTextField = new JTextField(String.valueOf(this.n));

        // Add the input boxes and labels to the input panel
        inputPanel.add(niLabel);
        inputPanel.add(niTextField);
        inputPanel.add(maxlLabel);
        inputPanel.add(maxlTextField);
        inputPanel.add(nLabel);
        inputPanel.add(nTextField);

        // Add the input panel to the input panel
        inputBoxPanel.add(inputPanel, BorderLayout.CENTER);

        // Create a button to change parameters after N but before closing input
        JButton changeParametersButton = new JButton("Update Parameters");
        changeParametersButton.addActionListener(l -> {
            try {
                this.ni = Integer.parseInt(niTextField.getText());
                this.maxl = Integer.parseInt(maxlTextField.getText());
                this.n = Integer.parseInt(nTextField.getText());
                this.controller.setNi(this.ni);
                this.controller.setMaxl(this.maxl);
                this.controller.setN(this.n);
            } catch (NumberFormatException er) {
                JOptionPane.showMessageDialog(GuiViewImpl.this, "NI, MAXL, N should be numbers", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add the button to the input panel
        inputBoxPanel.add(changeParametersButton, BorderLayout.SOUTH);

        // Add the input panel to the JFrame
        add(inputBoxPanel, BorderLayout.CENTER);


        // Create a JPanel for the lists and buttons
        JPanel listPanel = new JPanel(new BorderLayout());

        // Create the left and right lists
        longestFiles = new JList<>();
        longestFiles.setVisibleRowCount(this.n);
        JScrollPane longestFilesScrollPane = new JScrollPane(longestFiles);
        longestFilesScrollPane.setBorder(BorderFactory.createTitledBorder("Longest Files"));
        listPanel.add(longestFilesScrollPane);

        linesCounters = new JList<>();
        linesCounters.setVisibleRowCount(this.ni + 1);
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
        startButton.addActionListener(l -> {
            try {
                this.controller.startGui(this.d);
            } catch (OperationNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        stopButton = new JButton("Stop");
        stopButton.addActionListener(l -> this.controller.stop());

        // Add the buttons to the button panel
        buttonPanel.add(stopButton);
        buttonPanel.add(startButton);
        listPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the list panel below the input boxes
        add(listPanel, BorderLayout.SOUTH);

        // Set the JFrame to be visible
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.initialized = true;

    }

    @Override
    public void show(Pair<Map<Pair<Integer, Integer>, UnmodifiableCounter>, Collection<FileInfo>> result) throws OperationNotSupportedException {
        if (!initialized) {
            throw new OperationNotSupportedException("Gui not yet initialized");
        }
        SwingUtilities.invokeLater(() -> {
            this.linesCounters.setListData(result.getX().entrySet().stream()
                    .sorted(Comparator.comparingInt(e -> e.getKey().getX()))
                    .map(e ->
                            e.getValue().getValue() +
                                    " files in range [" +
                                    e.getKey().getX() +
                                    (e.getKey().getY() == Integer.MAX_VALUE ? "+" : ("," + e.getKey().getY())) +
                                    "]"
                    )
                    .toArray(String[]::new));
            this.longestFiles.setListData(
                    result.getY().stream()
                            .sorted(Comparator.comparingLong(FileInfo::getLineCount))
                            .map(f -> f.getFile().getName() + " -> " + f.getLineCount())
                            .toArray(String[]::new)
            );
        });
    }
}
