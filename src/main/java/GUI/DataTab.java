//package GUI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class DataTab extends JPanel {
//    Utility util = new Utility();
//    private static Path DATA_DIR = null;
//
//    public DataTab(Utility util) {
//        this.util = util;
//        DATA_DIR = util.getSQMSaveDirFromConfig().toAbsolutePath();
//        setSize(800, 560);
//        setLayout(new BorderLayout());
//
//        JLabel label = new JLabel("Browse data files in: " + DATA_DIR);
//        JFileChooser fileChooser = new JFileChooser(DATA_DIR.toFile());
//        fileChooser.setControlButtonsAreShown(false); // hides approve/cancel buttons
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//
//        JButton openDir = new JButton("Open Folder in File Explorer");
//        openDir.addActionListener(e -> {
//            try {
//                Desktop.getDesktop().open(DATA_DIR.toFile());
//            } catch (IOException ex) {
//                util.append("[GUI] " + ex.getMessage());
//            }
//        });
//
//        JPanel topPanel = new JPanel(new BorderLayout());
//        topPanel.add(label, BorderLayout.CENTER);
//        topPanel.add(openDir, BorderLayout.EAST);
//        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//
//        add(topPanel, BorderLayout.NORTH);
//        add(fileChooser, BorderLayout.CENTER);
//    }
//}
package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataTab extends JPanel {
    private Utility util;
    private static Path DATA_DIR = null;
    private JFileChooser fileChooser;

    public DataTab(Utility util) {
        this.util = util;
        DATA_DIR = util.getSQMSaveDirFromConfig().toAbsolutePath();

        setSize(800, 560);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Browse data files in: " + DATA_DIR);
        fileChooser = new JFileChooser(DATA_DIR.toFile());
        fileChooser.setControlButtonsAreShown(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        JButton openDir = new JButton("Open Folder in File Explorer");
        openDir.addActionListener(e -> {
            File selected = fileChooser.getSelectedFile();
            if (selected == null) selected = DATA_DIR.toFile();
            if (selected.isFile()) selected = selected.getParentFile();
            try {
                label.setText(selected.getAbsolutePath());
                Desktop.getDesktop().open(selected);
            } catch (IOException ex) {
                util.append("[GUI] Failed to open folder: " + ex.getMessage());
            }
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            File currentDir = fileChooser.getCurrentDirectory();
            fileChooser.setCurrentDirectory(null); // force refresh
            fileChooser.setCurrentDirectory(currentDir);
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        buttonPanel.add(openDir);

        topPanel.add(label, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(topPanel, BorderLayout.NORTH);
        add(fileChooser, BorderLayout.CENTER);
    }
}
