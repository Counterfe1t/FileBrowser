import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainWindow extends JFrame implements ActionListener {

    private final FileBrowserClient client;
    private File file;
    private int[] fileSizes;
    private static String SERVER_HOME_DIRECTORY;

    // Panels
    private static JPanel mainPanel;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel bottomPanel;

    // List
    private static JList<File> list;
    private DefaultListModel<File> model;
    private JScrollPane scrollPane;

    // Buttons
    private JButton backButton;
    private static JButton chooseButton;

    // Labels
    private JLabel filePathLabel;
    private static JLabel fileSizeLabel;

    // TextAreas
    private static JTextArea pathArea;

    public MainWindow(final FileBrowserClient client) throws IOException, ClassNotFoundException {

        this.client = client;
        file = client.getDirectory();
        SERVER_HOME_DIRECTORY = file.getCanonicalPath();
        fileSizes = client.getFileSizeList();

        URL url = getClass().getResource("/icon_window.png");
        this.setTitle("FileBrowser");
        this.setSize(new Dimension(700, 500));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setIconImage(new ImageIcon(url).getImage());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Panel for holding all components
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Top panel
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        // Simple label
        filePathLabel = new JLabel("Path:");
        filePathLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        //topPanel.add(filePathLabel, BorderLayout.LINE_START);
        topPanel.add(filePathLabel, FlowLayout.LEFT);

        // Text area for displaying path to current directory
        pathArea = new JTextArea();
        pathArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pathArea.setText(" " + file.getCanonicalPath().substring(60));
        pathArea.setEditable(false);
        pathArea.setBorder(new LineBorder(Color.BLACK));
        pathArea.setPreferredSize(new Dimension(580, 20));
        //topPanel.add(pathArea, BorderLayout.CENTER);
        topPanel.add(pathArea, FlowLayout.CENTER);

        // Middle panel
        middlePanel = new JPanel();

        // List holding contents of current directory
        list = new JList<>();
        this.setupFileList(file);
        FileListCellRenderer fileListCellRenderer = new FileListCellRenderer();
        //list.setBorder(new LineBorder(Color.BLACK));
        list.setCellRenderer(fileListCellRenderer);
        list.setFixedCellWidth(600);
        list.setFixedCellHeight(22);
        list.setVisibleRowCount(13);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chooseButton.doClick();
                }
            }
        });

        // Display size of the currently selected file
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                File f = list.getSelectedValue();
                if(f != null) {
                    if(f.isDirectory()) {
                        fileSizeLabel.setText("Size: Directory");
                        chooseButton.setText("Enter");
                    }
                    else if(f.isFile()) {
                        fileSizeLabel.setText("Size: " + String.valueOf(fileSizes[list.getSelectedIndex()]) + " bytes");
                        chooseButton.setText("Download");
                    }
                }
            }
        });

        scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        middlePanel.add(scrollPane);

        // Bottom panel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        //bottomPanel.setMaximumSize(new Dimension(660, 200));

        // Filler invisible component
        bottomPanel.add(Box.createRigidArea(new Dimension(5, 35)));

        // Label for displaying file size
        fileSizeLabel = new JLabel("Size:");
        fileSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        bottomPanel.add(fileSizeLabel);

        // Filler invisible component
        bottomPanel.add(Box.createRigidArea(new Dimension(5, 35)));

        // Filler invisible component
        bottomPanel.add(Box.createHorizontalGlue());

        // BACK button
        backButton = new JButton("Back");
        backButton.setEnabled(false);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        backButton.setToolTipText("Enter previous directory");
        backButton.addActionListener(this);
        bottomPanel.add(backButton);

        // Filler invisible component
        bottomPanel.add(Box.createRigidArea(new Dimension(7, 0)));

        // CHOOSE button
        chooseButton = new JButton("Choose");
        chooseButton.setPreferredSize(new Dimension(100, 20));
        chooseButton.setFocusPainted(false);
        chooseButton.setToolTipText("Enter selected directory or download selected file");
        chooseButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        chooseButton.addActionListener(this);
        bottomPanel.add(chooseButton);

        // Filler invisible component
        bottomPanel.add(Box.createRigidArea(new Dimension(5, 0)));


        // Close sockets after closing main window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    if(JOptionPane.showConfirmDialog(mainPanel,
                            "Are you sure?",
                            "Window closing",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(getClass().getResource("/icon_question.png"))) == JOptionPane.YES_OPTION) {

                        new SystemSound(2).play();
                        client.sendMSG("exit");
                        client.socket.close();
                        System.exit(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add to main panel
        mainPanel.add(topPanel);
        mainPanel.add(middlePanel);
        mainPanel.add(bottomPanel);

        // Set main panel as the main frame component
        setContentPane(mainPanel);
        pack();

        new SystemSound(1).play();
    }

    // Fill JList with subdirectories of given file
    private void setupFileList(File file) {
        model = new DefaultListModel<>();

        for (File f : file.listFiles()) {
            model.addElement(f);
        }

        list.setModel(model);
    }

    // Set path to currently viewed directory
    private void setCurrentPath(File file) {
        try {
            PathShortener pathShortener = new PathShortener();
            pathArea.setText(" " + pathShortener.pathShortener(file.getCanonicalPath().substring(60), 5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        // CHOOSE button listener
        if(event.getSource() == chooseButton) {
            try {
                if(list.getSelectedValue() != null) {

                    File selectedFile = list.getSelectedValue();

                    if (selectedFile.isDirectory()) {
                        client.sendMSG(list.getSelectedValue().toString());
                        file = client.getDirectory();
                        fileSizes = client.getFileSizeList();
                        setupFileList(file);
                        setCurrentPath(file);
                        new SystemSound(3).play();

                        if(!backButton.isEnabled()) {
                            backButton.setEnabled(true);
                        }
                    } else if (selectedFile.isFile()) {
                        new SaveFileWindow(client, selectedFile);
                    }
                }
            } catch (ClassNotFoundException | IOException  e) {
                e.printStackTrace();
            }

        }

        // BACK button listener
        if(event.getSource() == backButton) {
            try {
                if(file.getParent().equals(SERVER_HOME_DIRECTORY)) {
                    client.sendMSG(file.getParent());
                    file = client.getDirectory();
                    fileSizes = client.getFileSizeList();
                    setupFileList(file);
                    setCurrentPath(file);
                    new SystemSound(3).play();

                    backButton.setEnabled(false);
                } else {
                    client.sendMSG(file.getParent());
                    file = client.getDirectory();
                    fileSizes = client.getFileSizeList();
                    setupFileList(file);
                    setCurrentPath(file);
                    new SystemSound(3).play();
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
