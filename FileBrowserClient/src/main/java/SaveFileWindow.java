import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class SaveFileWindow extends JFrame implements ActionListener {

    private FileBrowserClient client;
    private File fileToSave;
    private File file;
    private Path path;

    // Panels
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel bottomPanel;

    // List
    private static JList<File> list;
    private DefaultListModel<File> model;
    private JScrollPane scrollPane;

    // Buttons
    private JButton enterButton;
    private JButton backButton;
    private JButton saveButton;

    // Labels
    private JLabel pathLabel;

    // TextAreas
    private JTextArea pathArea;

    // Setters
    private void setFileBrowserClient(FileBrowserClient client) {
        this.client = client;
    }
    private void setFileToSave(File fileToSave) {
        this.fileToSave = fileToSave;
    }
    private void setFile() {
        this.file = new File(System.getProperty("user.home"));
    }
    private void setPath() {
        this.path = file.toPath();
    }

    // Getters
    public File getFile() {
        return file;
    }

    public SaveFileWindow(FileBrowserClient client, File fileToSave) throws IOException {

        this.setFileBrowserClient(client);
        this.setFileToSave(fileToSave);
        this.setFile();
        this.setPath();

        URL url = getClass().getResource("/icon_window.png");
        this.setTitle("FileBrowser - Save file to specific directory");
        this.setSize(new Dimension(550, 400));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setIconImage(new ImageIcon(url).getImage());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Panels
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(2, 5, 6, 5));
        //mainPanel.setBackground(Color.BLUE);

        topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(550, 400));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        //topPanel.setBackground(Color.GREEN);

        middlePanel = new JPanel();
        middlePanel.setMaximumSize(new Dimension(550, 400));
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        //middlePanel.setBackground(Color.YELLOW);

        bottomPanel = new JPanel();
        bottomPanel.setMaximumSize(new Dimension(550, 400));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(new EmptyBorder(7, 0, 0, 0));
        //bottomPanel.setBackground(Color.GREEN);

        // Labels
        pathLabel = new JLabel("Path:");
        pathLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        topPanel.add(pathLabel);

        // Filler invisible component
        topPanel.add(Box.createRigidArea(new Dimension(5, 30)));

        // TextAreas
        pathArea = new JTextArea();
        pathArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pathArea.setText(" " + path.toString());
        pathArea.setEditable(false);
        pathArea.setBorder(new LineBorder(Color.BLACK));
        pathArea.setMaximumSize(new Dimension(500, 20));
        topPanel.add(pathArea);

        // List holding contents of current directory
        list = new JList<>();
        this.setupFileList(file);
        SaveFileListCellRenderer saveFileListCellRenderer = new SaveFileListCellRenderer();
        list.setCellRenderer(saveFileListCellRenderer);
        list.setFixedCellWidth(500);
        list.setFixedCellHeight(22);
        list.setVisibleRowCount(10);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    enterButton.doClick();
                }
            }
        });

        scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        middlePanel.add(scrollPane);

        // Buttons
        backButton = new JButton("Back");
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        backButton.setToolTipText("Enter previous directory");
        backButton.addActionListener(this);
        bottomPanel.add(backButton);

        // Filler invisible component
        bottomPanel.add(Box.createRigidArea(new Dimension(319, 0)));

        enterButton = new JButton("Enter");
        enterButton.setFocusPainted(false);
        enterButton.setToolTipText("Enter selected directory");
        enterButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        enterButton.addActionListener(this);
        bottomPanel.add(enterButton);

        // Filler invisible component
        bottomPanel.add(Box.createRigidArea(new Dimension(7, 0)));

        saveButton = new JButton("Save");
        saveButton.setFocusPainted(false);
        saveButton.setToolTipText("Save file in current directory");
        saveButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        saveButton.addActionListener(this);
        bottomPanel.add(saveButton);

        // Close sockets after closing main window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if(JOptionPane.showConfirmDialog(mainPanel,
                        "Are you sure?",
                        "Window closing",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon(getClass().getResource("/icon_question.png"))) == JOptionPane.YES_OPTION) {

                    dispose();
                }
            }
        });

        // Add components to main panel
        mainPanel.add(topPanel);
        mainPanel.add(middlePanel);
        mainPanel.add(bottomPanel);

        // Set main panel for window frame
        setContentPane(mainPanel);
        pack();
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
        path = file.toPath();
        PathShortener pathShortener = new PathShortener();
        pathArea.setText(" " + pathShortener.pathShortener(path.toString(), 4));
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        // BACK button listener
        if(event.getSource() == backButton) {
            new SystemSound(3).play();
            if(file.getParentFile() != null) {
                file = file.getParentFile();
                setupFileList(file);
                setCurrentPath(file);
            } else {
                new DialogWindow(this, 2);
                backButton.setEnabled(false);
            }

        }

        // ENTER button listener
        if(event.getSource() == enterButton) {
            if(list.getSelectedValue() != null) {
                File f = list.getSelectedValue();
                if(f != null) {
                    if(f.isDirectory()) {
                        file = f;
                        setupFileList(file);
                        setCurrentPath(file);
                        new SystemSound(3).play();
                    }
                    if(f.isFile()) {
                        // You cannot enter a file
                    }
                }

                if(!backButton.isEnabled()) {
                    backButton.setEnabled(true);
                }
            }
        }

        // SAVE button listener
        if(event.getSource() == saveButton) {
            // Return path to specified directory
            try {
                client.sendMSG(fileToSave.getCanonicalPath());
                int fileSize = client.getFileSize();
                client.receiveFile(fileToSave.getName(), path, fileSize);
                new DialogWindow(this, 3);

                dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
