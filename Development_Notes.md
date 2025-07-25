# Development Notes

## project structure:
```
project-root/
│
├── app/
│   ├── win/                    ← Packaged app for Windows users
│   │
│   ├── mac/                    ← Packaged app for MacOS users
│   │
│   └── lin/                    ← Packaged app for Linux users
│
│
│
├── src/
│   └── main/
│       ├── java/               ← Java GUI source
│       │   └── GUI/
│       │       ├── profiles/   ← saved to file location chosen by user, default in this folder
│       │       │   │
│       │       │   └── pi_profile.properties
│       │       │
│       │       ├── BuildGUI.java
│       │       ├── RpiCommandTab.java
│       │       ├── SettingsTab.java
│       │       ├── DataTab.java
│       │       ├── DataTab.java
│       │       ├── HelpTab.java
│       │       ├── SetupWizard.java
│       │       ├── Utility.java
│       │       │
│       │       └── host_config.properties
│       │
│       └── resources/
│           ├── icon/           ← image files for app icons
│           │
│           ├── setup/          ← image files for app icons
│           │   │
│           │   └── installer.iss/
│           │
│           │
│           │
│           ├── python/         ← bundled embedded python
│           │
│           └── python-scripts/ ← Python scripts, profiles/, configs.py
│               ├── modem/
│               │   ├── test.py
│               │   └── ssh.sh
│               │
│               ├── Py3SQM/
│               │
│               ├── ssh/
│               │   ├── auto_setup.py
│               │   ├── configs_ssh.py
│               │   ├── first_time_setup.py
│               │   ├── host_ssh.py
│               │   ├── lora_child_ssh.py
│               │   ├── lora_parent_ssh.py
│               │   ├── rpi_ssh.py
│               │   ├── sensor_ssh.py
│               │   ├── sensor_stream.py
│               │   ├── ui_commands.py
│               │   ├── radio_runner.sh
│               │   ├── rpi_runner.sh
│               │   └── sensor_runner.sh
│               │
│               └── scripts/
│                   ├── runradio.sh
│                   ├── runrpi.sh
│                   ├── runsensor.sh
│                   └── cronjobs.txt
│
├── jre/                       ← Bundled JRE (Java Runtime Environment)
│
│
├── pom.xml                    ← Maven build settings and plugins here
└── target/                    ← Output .jar/.exe
```
As the backend script runs, it establishes an SSH connection to the Raspberry Pi using credentials defined in a configuration file. It executes the requested remote shell commands or Python routines on the sensor node, then relays output and error messages back to its own standard output. Meanwhile, the Java GUI continuously reads this output stream using an InputStreamReader inside a dedicated background thread. Each line received from the backend is appended to a live console panel embedded in the GUI, providing users with immediate feedback about the command’s progress and outcome.


### `BuildGUI.java`
Contains the constructor for the full GUI
* calls each of `RPiCommandTab.java`, `SensorCommandTab.java`, `DataTab.java`, etc. to build the tabs of the GUI
* uses JTabbed Pane to separate each of the tabs
* builds one global console where backend information is displayed
* includes methods to minimize and show the console

### `RPiCommandTab.java`
Manage core RPi processes like starting/stopping the listener, checking status, and syncing files with the host, and sensor processes like requesting readings, calibration, etc.
* contains 3 main parts: the built-in commands, the manual command field, and the input field.
    * there are 4 built in commands located at the top of the RPi Command Center Tab to communicate directly with the RPi and a dropdown menu for specific commands to control the sensor.
    * the manual command field located at the bottom of the RPi Command Tab allows you to type in a command.
    * the input field becomes active for commands that require user input: if command requires user input, the input panel will prompt the user to supply more information

**parameters**:
* utility &rarr; one common utility method that is built in GUI.java
    * two main panels: command panel and feedback panel

**return**


### `DataTab.java`
Contains the constructor for the Data Tab
* **parameters**:
    * utility &rarr; one common utility method that is built in GUI.java
* using file chooser to allow user to navigate through their files to see where sqm data is synced to
* opens the user's file explorer to allow them to actually see the data
* **return**

### `SettingsTab.java`
Contains the constructor for the Settings Tab
* **parameters**:
    * utility &rarr; one common utility method that is built in GUI.java
* **other methods**
    * `loadAllProfiles()`
    * `loadProfile(String profileName)`
    * `saveProfile()`
    * `addProfile()`
    * `deleteProfile()`
* **return**

### `HelpTab.java`
This tab goes more in depth on each of the GUI tabs and how to use it.
* dropdown menu to access pages on more specific parts of the help center:
    * General
    * Rpi Command
    * Data Sync
    * Settings
    * SSH Help

**parameters**:
* utility &rarr; one common utility method that is built in GUI.java

**returns**

### `Utility.java`
Contains the utility methods used across all GUI tabs
* `sendCommand(String cmd)`
  * Runs the Python backend script with a given command and streams the output to the GUI console.
  * How it works:
    * Validates the command is not blank. 
    * Builds a `ProcessBuilder` with the Python interpreter, backend script, and the command. 
    * Starts the process, reads its stdout line by line, appends it to the GUI console, and shows a toast indicating success or error.

* `append(String txt)`
  * Safely appends a line of text to the GUI console on the Swing event thread.
  * How it works:
    * Uses `SwingUtilities.invokeLater` to ensure thread safety. 
    * Adds the text and auto-scrolls to the end.

* `updateConfigsPy(String RpiName, String RpiAddr)`
  * Updates the `configs_ssh.py` file to change `rpi_name` and `rpi_addr` variables.
  * **How it works:**
    * Reads the file line by line. 
    * Replaces lines starting with `rpi_name =` or `rpi_addr =` with new values. 
    * Writes the updated content back to the same file.

* `wrapWithRightPanel(JPanel main, JPanel side)`
  * Wraps a main panel with a right-side panel for layout purposes (side panel on the EAST).
  * How it works:
    * Creates a `BorderLayout` container. 
    * Puts the main in the `CENTER`. 
    * Sets a preferred width for the side and places it in the `EAST`.

* `startPythonBackend()`
    *
* `copyToClipboard(String text)`
    * Copies the given text to the system clipboard.
    * How it works:
      * Uses `Toolkit` and `StringSelection`.

* Getters:
  * `getProfileSaveDirFromConfig()`
    * Reads the profile_save_path property from host_config.properties.
    * How it works:
      * Loads the properties file. 
      * Returns the Path if present and not blank; otherwise null.

  * `getSQMSaveDirFromConfig()`
    * Same as above, but for the sqm_data_path property.

  * `getDetectedOSType()`
    * Parses host_config.properties to get the saved OS type (OS= line).
    * How it works:
      * Reads the file line by line. 
      * If OS= line found, returns the value in lowercase. 
      * If not found or error, returns "unknown".

* Builders:
  * `buildTextArea(JPanel panel, int height)`
    * Creates a read-only, word-wrapped JTextArea sized to match the design’s preferred width.
    * How it works:
      * Sets background same as parent. 
      * Uses preferredWidth and setFullWidth helper.

  * `buildCopyRow(String command, int height)`
    * Creates a reusable UI row with a read-only text field and a "Copy" button for that command.
    * How it works:
      * Uses BoxLayout for horizontal arrangement. 
      * Copy button copies the command to clipboard. 
      * Constrains max width for layout consistency.

### `SetupWizard.java`
### `setup_linux.java`
### `setup_windows.java`
### `setup_mac.java`

---

## why java?
### cross-platform compatibility
runs on Windows, macOS, and Linux without needing to rewrite the UI code.

ideal when the GUI needs to be used from different machines or OSes to control the Raspberry Pi.

### strong UI toolkit with swing
swing provides a rich set of widgets (buttons, tabs, text areas, dialogs, etc.) that are sufficient for the needs of:
* command buttons
* real-time logs
* file listing and settings forms


### maintainable and modular
GUI logic is separated from backend logic (handled in Python), allowing easier development and debugging on both ends.

### widely known/easy accessible
java is commonly taught and widely used, so it's easier for other developers to maintain or extend.
It avoids forcing the user to install and configure a web server, browser-based interface, and is taught in Smith's CS curriculum so avoids having to learn a new language.

## packaging with maven
This project uses maven as a project/build manager.



```java
//    private JPanel buildSSH_Step1(){
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JPanel inner = new JPanel();
//        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
//
//        // STEP 1: CHECK IS SSH INSTALLED< IF IT's NOT, INSTALL IT
//        JPanel step1 = new JPanel();
//        step1.setLayout(new BoxLayout(step1, BoxLayout.Y_AXIS));
//        step1.add(new JLabel("Step 1: Check if SSH is installed"));
//        step1.add(Box.createRigidArea(new Dimension(0, 10)));
//
//        JTextArea copyI1 = util.buildTextArea(step1, 45);
//        copyI1.setText("Open up a new terminal. First, check whether or not you have ssh installed. This looks different on different operating systems. Your operating system is "+". If that does not seem right....");
//        String sshCmd = "";
//        JTextArea proceedI1 = util.buildTextArea(step1, 45);
//        switch (detectedOS) {
//            case "windows" -> {
//                sshCmd = "ssh";
//                proceedI1.setText("For windows users, there should be a line printed in the output of the terminal after running the first command that says 'Active: active (running)' if SSH is active. If this is the case, then move onto [STEP 2]. If not, move on to [STEP 2a].");
//            }
//            case "mac"     -> {
//                sshCmd = "sudo systemsetup -getremotelogin";
//                proceedI1.setText("For linux users, there should be a line printed in the output of the terminal after running the first command that says 'Active: active (running)' if SSH is active. If this is the case, then move onto [STEP 2]. If not, move on to [STEP 2a].");
//            }
//            case "linux"   -> {
//                sshCmd = "systemctl status sshd";
//                proceedI1.setText("For linux users, there should be a line printed in the output of the terminal after running the first command that says 'Active: active (running)' if SSH is active. If this is the case, then move onto [STEP 2]. If not, move on to [STEP 2a].");
//            }
//        }
//        JPanel checkSSHRow = buildCopyRow(sshCmd);
//
//        step1.add(copyI1);
//        step1.add(Box.createRigidArea(new Dimension(0, 10)));
//        step1.add(checkSSHRow);
//        step1.add(Box.createRigidArea(new Dimension(0, 10)));
//        step1.add(proceedI1);
//        step1.add(Box.createRigidArea(new Dimension(0, 30)));
//
//        // STEP 1a: DOWNLOAD SSH IF NOT DOWNLOADED
//        JPanel step1a = new JPanel();
//        step1a.setLayout(new BoxLayout(step1a, BoxLayout.Y_AXIS));
//        step1a.add(new JLabel("Step 1a: Download SSH"));
//        step1a.add(Box.createRigidArea(new Dimension(0, 10)));
//
////        JTextArea copyI1a = util.buildTextArea(step1, 15);
////        copyI1.setText("If you do not already have ssh installed: ");
////        step1a.add(copyI1a);
////        step1a.add(Box.createRigidArea(new Dimension(0, 10)));
//
//        switch (detectedOS) {
//            case "windows" -> {
//                JTextArea download = util.buildTextArea(step1, 200);
//                download.setText("""
//                        If SSH is installed, it will display help information about the command, including its usage and available options. If the terminal returns: "SSH not recognized", then this means SSH is not installed or enabled on your system. Follow these steps to download ssh:
//
//                        1) Go to Settings > Apps > Apps & Features > Optional Features
//
//                        2) Click on "Add a feature" and select OpenSSH Client
//
//                        3) Click "Install" to add the OpenSSH Client to your system
//
//                        4) You may need to restart your system for the changes to take effect
//
//                        5) After installing OpenSSH Client, open a new command prompt or PowerShell and type `ssh` again to confirm that it is now recognized.
//                        """);
//                step1a.add(download);
//                step1a.add(Box.createRigidArea(new Dimension(0, 10)));
//            }
//            case "mac"     -> {
//                JTextArea download = util.buildTextArea(step1, 200);
//                download.setText("""
//                Mac operating systems should come pre-installed with SSH. If SSH is enabled, the output will show "Remote Login: On". If it's off, it will show "Remote Login: Off". SSH may be disabled on your system. In that case, follow these steps to enable it:
//
//                1) Open System Preferences (Apple menu > System Preferences)
//
//                2) Go to Sharing
//
//                3) Look for "Remote Login" on the left-hand side
//
//                4) To enable SSH, activate the checkmark next to "Remote Login"
//                """);
//                step1a.add(download);
//                step1a.add(Box.createRigidArea(new Dimension(0, 10)));
//            }
//            case "linux"   -> {
//                JTextArea download = util.buildTextArea(step1, 45);
//                download.setText("""
//                If the terminal says anything along the lines of "Unit file sshd.service does not exist", then you do not have ssh installed. Run the following command in your terminal to install SSH:
//                """);
//                step1a.add(download);
//                step1a.add(Box.createRigidArea(new Dimension(0, 10)));
//
//                String downloadCmd = "sudo apt install openssh-server openssh-client";
//                JPanel downloadSSHRow = buildCopyRow(downloadCmd);
//                step1a.add(downloadSSHRow);
//                step1a.add(Box.createRigidArea(new Dimension(0, 10)));
//
//                JTextArea enable = util.buildTextArea(step1, 45);
//                enable.setText("""
//                It may be the case that SSH is installed but is not active. To enable SSH, run the following command in your terminal:
//                """);
//                step1a.add(download);
//                step1a.add(Box.createRigidArea(new Dimension(0, 10)));
//
//                String enableCmd = "sudo systemctl start ssh";
//                JPanel enableSSHRow = buildCopyRow(enableCmd);
//                step1a.add(enableSSHRow);
//                step1a.add(Box.createRigidArea(new Dimension(0, 20)));
//            }
//        }
//
//        inner.add(step1);
//        inner.add(step1a);
//
//        JScrollPane scroll = new JScrollPane(inner);
//        scroll.setBorder(null);
//
//        panel.add(scroll);
//        return panel;
//    }
```