import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class ConnectWindow extends JFrame implements ActionListener {

    // Fields representing address and port number
    private String address = null;
    private int port = -1;

    // Panels
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel bottomPanel;

    // Labels
    private JLabel addressLabel;
    private JLabel portLabel;

    // TextAreas
    private JTextField addressArea;
    private JTextField portArea;

    // Buttons
    private JButton connectButton;

    // Setters
    private void setAddress(String address) {
        this.address = address;
    }
    private void setPort(int port) {
        this.port = port;
    }

    // Getters
    public String getAddress() {
        return address;
    }
    public int getPort() {
        return port;
    }

    public ConnectWindow() {
        URL url = getClass().getResource("/icon_window.png");
        this.setTitle("FileBrowser - Connect to server");
        this.setSize(new Dimension(500, 200));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setIconImage(new ImageIcon(url).getImage());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Panels
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(6, 10, 0, 6));

        topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(400, 50));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        middlePanel = new JPanel();
        middlePanel.setMaximumSize(new Dimension(400, 50));
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));

        bottomPanel = new JPanel();
        bottomPanel.setMaximumSize(new Dimension(400, 50));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(new EmptyBorder(7, 0, 0, 0));

        // Labels
        addressLabel = new JLabel("Address: ");
        addressLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        topPanel.add(addressLabel);

        // Filler invisible component
        middlePanel.add(Box.createRigidArea(new Dimension(22, 30)));

        portLabel = new JLabel("Port: ");
        portLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        middlePanel.add(portLabel);

        // Text Areas
        addressArea = new JTextField();
        addressArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        addressArea.setText("");
        addressArea.setEditable(true);
        addressArea.setBorder(new LineBorder(Color.BLACK));
        addressArea.setMaximumSize(new Dimension(300, 20));


        // AddressArea kay listener
        KeyListener addressKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int max = 22;
                if(addressArea.getText().length() >= max) {
                    e.consume();
                    String shortened = addressArea.getText().substring(0, max);
                    addressArea.setText(shortened);
                }
                else if(addressArea.getText().length() > max) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connectButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        };

        addressArea.addKeyListener(addressKeyListener);
        topPanel.add(addressArea);

        // Filler invisible component
        topPanel.add(Box.createRigidArea(new Dimension(50, 35)));

        portArea = new JTextField();
        portArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        portArea.setText("");
        portArea.setEditable(true);
        portArea.setBorder(new LineBorder(Color.BLACK));
        portArea.setMaximumSize(new Dimension(300, 20));

        // PortArea key listener
        KeyListener portKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int max = 9;
                if(portArea.getText().length() >= max) {
                    e.consume();
                    String shortened = portArea.getText().substring(0, max);
                    portArea.setText(shortened);
                }
                else if(portArea.getText().length() > max) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connectButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        };

        portArea.addKeyListener(portKeyListener);
        middlePanel.add(portArea);

        // Filler invisible component
        middlePanel.add(Box.createRigidArea(new Dimension(50, 35)));

        // Filler invisible component
        bottomPanel.add(Box.createRigidArea(new Dimension(250, 40)));

        connectButton = new JButton("Connect");
        connectButton.setFocusPainted(false);
        connectButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        connectButton.setToolTipText("Connect with the server");
        connectButton.addActionListener(this);
        bottomPanel.add(connectButton);

        // Close connection window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if(JOptionPane.showConfirmDialog(
                        mainPanel,
                        "Are you sure?",
                        "Window closing",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon(getClass().getResource("/icon_question.png"))) == JOptionPane.YES_OPTION) {

                    System.exit(0);
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

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == connectButton) {
            if(!addressArea.getText().isEmpty() || !portArea.getText().isEmpty()) {
                try {
                    setAddress(addressArea.getText());
                    setPort(Integer.valueOf(portArea.getText()));

                    // If no exception has been caught
                    dispose();
                } catch (NumberFormatException e) {
                    //new ErrorDialog(this, 0);
                }
            }

        }
    }
}
