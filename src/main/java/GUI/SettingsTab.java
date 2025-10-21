package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class SettingsTab extends JPanel {
    private final Utility util;
    private final DefaultListModel<String> profileListModel = new DefaultListModel<>();
    private final JList<String> profileList = new JList<>(profileListModel);
    private final JTextField rpiNameField = new JTextField();
    private final JTextField rpiAddrField = new JTextField();
    private final Properties currentProps = new Properties();
    private String currentProfile;
    private final JTextField profilePathField = new JTextField();
    private final JTextField dataPathField = new JTextField();


    public SettingsTab(Utility util) {
        this.util = util;
        setLayout(new BorderLayout());

        // Left panel: profile list and add button
        JPanel leftPanel = new JPanel(new BorderLayout());
        profileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadProfile(profileList.getSelectedValue());
            }
        });
        JScrollPane listScroll = new JScrollPane(profileList);
        leftPanel.add(listScroll, BorderLayout.CENTER);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel rpis = new JLabel("Raspberry Pi Profiles:");
        topLeft.add(rpis);

        JButton addButton = new JButton("+");
        addButton.setToolTipText("Add new profile");
        addButton.addActionListener(e -> addProfile());
        JButton sshButton = new JButton("?");
        sshButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new SSHInstructions(util).setVisible(true));
        });
        JPanel bottomLeft = new JPanel(new BorderLayout());
        bottomLeft.add(addButton, BorderLayout.WEST);
        bottomLeft.add(sshButton, BorderLayout.EAST);
        leftPanel.add(topLeft, BorderLayout.NORTH);
        leftPanel.add(bottomLeft, BorderLayout.SOUTH);

        // Right panel: profile details and action buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rightPanel.add(new JLabel("RPi Name (rpi_name):"));
        rightPanel.add(rpiNameField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        rightPanel.add(new JLabel("RPi Address (rpi_addr):"));
        rightPanel.add(rpiAddrField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveProfile());
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteProfile());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(buttonPanel);

        // ==== File Path Configuration Section ====
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BoxLayout(pathPanel, BoxLayout.Y_AXIS));
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        pathPanel.setBorder(BorderFactory.createTitledBorder(border, "File Path Configuration"));
        pathPanel.add(Box.createRigidArea(new Dimension(0, 20)));


// Load initial paths from host_config.properties
        Path profileDir = util.getProfileSaveDirFromConfig();
        Path dataDir = util.getSQMSaveDirFromConfig();
        if (profileDir != null) profilePathField.setText(profileDir.toString());
        if (dataDir != null) dataPathField.setText(dataDir.toString());

// Profile path row
        JPanel profileRow = new JPanel(new BorderLayout(5, 5));
        profileRow.add(new JLabel("Profile Save Path:"), BorderLayout.WEST);
        profileRow.add(profilePathField, BorderLayout.CENTER);
        JButton browseProfileBtn = new JButton("Browse");
        browseProfileBtn.addActionListener(e -> {
            assert profileDir != null;
            choosePath(profilePathField, profileDir.toString());
        });
        profileRow.add(browseProfileBtn, BorderLayout.EAST);
        pathPanel.add(profileRow);
        pathPanel.add(Box.createRigidArea(new Dimension(0, 8)));

// SQM data path row
        JPanel dataRow = new JPanel(new BorderLayout(5, 5));
        dataRow.add(new JLabel("SQM Data Path:"), BorderLayout.WEST);
        dataRow.add(dataPathField, BorderLayout.CENTER);
        JButton browseDataBtn = new JButton("Browse");
        browseDataBtn.addActionListener(e -> {
            assert dataDir != null;
            choosePath(dataPathField, dataDir.toString());
        });
        dataRow.add(browseDataBtn, BorderLayout.EAST);
        pathPanel.add(dataRow);
        pathPanel.add(Box.createRigidArea(new Dimension(0, 10)));

// Save button for paths
        JButton savePathsBtn = new JButton("Save Paths");
        savePathsBtn.addActionListener(e -> savePathsToConfig());
        pathPanel.add(savePathsBtn);

        add(pathPanel, BorderLayout.SOUTH);


        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        loadAllProfiles();
    }

    private void loadAllProfiles() {
        profileListModel.clear();
        File profileDir = new File(util.getProfileSaveDirFromConfig().toString());
        if (!profileDir.exists()) return;

        String[] profileNames = profileDir.list((dir, name) -> name.endsWith("_profile.properties"));
        if (profileNames != null) {
            Arrays.stream(profileNames)
                    .map(name -> name.replace("_profile.properties", ""))
                    .sorted()
                    .forEach(profileListModel::addElement);
        }
    }

    private void loadProfile(String profileName) {
        if (profileName == null || profileName.isEmpty()) return;

        currentProfile = profileName;
        File profileFile = new File("profiles", profileName + "_profile.properties");
        try (FileReader reader = new FileReader(profileFile)) {
            currentProps.clear();
            currentProps.load(reader);
            rpiNameField.setText(currentProps.getProperty("rpi_name", ""));
            rpiAddrField.setText(currentProps.getProperty("rpi_addr", ""));
            util.append("[Settings] Loaded profile: " + profileName);
        } catch (IOException ex) {
            util.append("[Error] Failed to load profile: " + ex.getMessage());
        }
    }

    private void saveProfile() {
        if (currentProfile == null) {
            util.append("[Error] No profile selected.");
            return;
        }

        String rpiName = rpiNameField.getText().trim();
        String rpiAddr = rpiAddrField.getText().trim();

        if (!rpiName.isEmpty() && !rpiAddr.isEmpty()) {
            currentProps.setProperty("rpi_name", rpiName);
            currentProps.setProperty("rpi_addr", rpiAddr);
            File profileFile = new File("profiles", currentProfile + "_profile.properties");
            try (FileWriter writer = new FileWriter(profileFile)) {
                currentProps.store(writer, null);
                util.append("[Settings] Profile saved: " + currentProfile);
                util.updateConfigsPy(rpiName, rpiAddr);
            } catch (IOException ex) {
                util.append("[Error] Failed to save profile: " + ex.getMessage());
            }
        } else {
            util.append("[Error] One or more fields are empty.");
        }
    }

    private void addProfile() {
        String newProfileName = JOptionPane.showInputDialog(this, "Enter new profile name:");
        if (newProfileName != null && !newProfileName.trim().isEmpty()) {
            File newProfileFile = new File("profiles", newProfileName + "_profile.properties");
            if (newProfileFile.exists()) {
                util.append("[Error] Profile already exists.");
                return;
            }
            String newAddress = JOptionPane.showInputDialog(this, "Enter RPi Address:");
            try (FileWriter writer = new FileWriter(newProfileFile)) {
                Properties props = new Properties();
                props.setProperty("rpi_name", newProfileName);
                props.setProperty("rpi_addr", newAddress);
                props.store(writer, null);
                profileListModel.addElement(newProfileName);
                profileList.setSelectedValue(newProfileName, true);
                rpiNameField.setText(newProfileName);
                rpiAddrField.setText(newAddress);
                util.append("[Settings] New profile added: " + newProfileName);
                BuildGUI.refreshProfileList();

                JPanel panel = new JPanel(new BorderLayout());
                panel.add(new JLabel("Profile saved. To use SSH, you must set up key sharing."), BorderLayout.NORTH);

                JButton sshButton = new JButton("Click for instructions on SSH");
                sshButton.addActionListener(e -> {
                    SwingUtilities.invokeLater(() -> new SSHInstructions(util).setVisible(true));
                });

                panel.add(sshButton, BorderLayout.SOUTH);
                JOptionPane.showMessageDialog(this, panel, "SSH Setup Required", JOptionPane.INFORMATION_MESSAGE);


            } catch (IOException ex) {
                util.append("[Error] Failed to add profile: " + ex.getMessage());
            }
        }
    }

    private void deleteProfile() {
        if (currentProfile == null) {
            util.append("[Error] No profile selected to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure? This cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            File profileFile = new File("profiles", currentProfile + "_profile.properties");
            if (profileFile.exists() && profileFile.delete()) {
                util.append("[Settings] Profile deleted: " + currentProfile);
                profileListModel.removeElement(currentProfile);
                rpiNameField.setText("");
                rpiAddrField.setText("");
                currentProfile = null;
                BuildGUI.refreshProfileList();
            } else {
                util.append("[Error] Failed to delete profile file.");
            }
        }
    }

    private void choosePath(JTextField targetField, String currentPath) {
        JFileChooser chooser = new JFileChooser(currentPath);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select Directory");
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = chooser.getSelectedFile();
            targetField.setText(selectedDir.getAbsolutePath());
        }
    }

    private void savePathsToConfig() {
        String profilePath = profilePathField.getText().trim();
        String dataPath = dataPathField.getText().trim();

        if (profilePath.isEmpty() || dataPath.isEmpty()) {
            util.append("[Error] Both paths must be set before saving.");
            return;
        }

        Path configPath = Paths.get(System.getProperty("user.home"), "profiles", "host_config.properties");
        Properties props = new Properties();

        try {
            // Load existing config if it exists
            if (Files.exists(configPath)) {
                try (FileReader reader = new FileReader(configPath.toFile())) {
                    props.load(reader);
                }
            }

            // Update values
            props.setProperty("profile_save_path", profilePath);
            props.setProperty("sqm_data_path", dataPath);

            // Save updated config
            try (FileWriter writer = new FileWriter(configPath.toFile())) {
                props.store(writer, "Updated by SettingsTab");
            }

            util.append("[Settings] Updated host_config.properties with new paths.");
            util.showToast(this, "Paths saved successfully.", "success", 2000);

        } catch (IOException ex) {
            util.append("[Error] Failed to update host_config.properties: " + ex.getMessage());
            util.showToast(this, "Error saving paths.", "error", 2000);
        }
    }

}