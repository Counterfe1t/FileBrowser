import javax.swing.*;

public class DialogWindow {

    // Constant variables representing error id
    private final int INVALID_INPUT_VALUES = 0;
    private final int INCORRECT_ADDRESS_OR_PORT = 1;
    private final int ROOT_DIRECTORY_REACHED = 2;
    private final int FILE_DOWNLOADED = 3;

    public DialogWindow(JFrame parent, int errorID) {
        ImageIcon icon;

        switch (errorID) {
            case INVALID_INPUT_VALUES:
                new SystemSound(0).play();
                icon = new ImageIcon(getClass().getResource("/icon_error.png"));
                JOptionPane.showMessageDialog(
                        parent,
                        "Invalid input values!",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon
                );
                break;
            case INCORRECT_ADDRESS_OR_PORT:
                new SystemSound(0).play();
                icon = new ImageIcon(getClass().getResource("/icon_error.png"));
                JOptionPane.showMessageDialog(
                        parent,
                        "Incorrect address/hostname or port number!",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon
                );
                break;
            case ROOT_DIRECTORY_REACHED:
                new SystemSound(0).play();
                icon = new ImageIcon(getClass().getResource("/icon_warning.png"));
                JOptionPane.showMessageDialog(
                        parent,
                        "Root directory reached!",
                        "Warning",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon
                );
                break;
            case FILE_DOWNLOADED:
                new SystemSound(4).play();
                icon = new ImageIcon(getClass().getResource("/icon_information.png"));
                JOptionPane.showMessageDialog(
                        parent,
                        "File downloaded successfully!",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon
                );
        }
    }
}
