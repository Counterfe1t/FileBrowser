import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class FileListCellRenderer extends DefaultListCellRenderer {

    private FileSystemView fileSystemView;
    private JLabel label;
    private Color textSelectionColor = Color.BLACK;
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundSelectionColor = new Color(150, 255, 50);
    private Color backgroundNonSelectionColor = Color.WHITE;

    public FileListCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getListCellRendererComponent (
            JList list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {

        File file = (File)value;
        if(file.isDirectory()) {
            URL url = getClass().getResource("/icon_directory.png");
            label.setIcon(new ImageIcon(url));
            label.setText(file.getName());
        }
        else if(file.isFile()) {
            URL url = getClass().getResource("/icon_file.png");
            label.setIcon(new ImageIcon(url));
            label.setText(file.getName());
        }

        label.setToolTipText(" " + file.getPath().substring(74));
        label.setFont(new Font("Tahoma", Font.PLAIN, 14));

        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }

        return label;
    }

}
