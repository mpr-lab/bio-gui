package GUI;

import javax.swing.*;
import java.awt.*;
public class setup_linux {
    private final Utility util;
    String detectedOS = "";

    public setup_linux(Utility util){
        this.util = util;
    }
    JPanel buildSSH_Step1(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // STEP 1: CHECK IS SSH INSTALLED< IF IT's NOT, INSTALL IT
        JPanel step1 = new JPanel();
        step1.setLayout(new BoxLayout(step1, BoxLayout.Y_AXIS));
        step1.add(new JLabel("Step 1: Check if SSH is installed"));
        step1.add(Box.createVerticalStrut(10));

        JTextArea copyI1 = util.buildTextArea(step1);
        copyI1.setText("Open up a new terminal. First, check whether or not you have ssh installed. This looks different on different operating systems. Your operating system is "+ detectedOS +". If that does not seem right....");
        String sshCmd = "systemctl status sshd";
        JPanel checkSSHRow = util.buildCopyRow(sshCmd);

        JTextArea proceedI1 = util.buildTextArea(step1);
        proceedI1.setText("For linux users, there should be a line printed in the output of the terminal after running the first command that says 'Active: active (running)' if SSH is active. If this is the case, then move onto [STEP 2]. If not, move on to [STEP 2a].");


        step1.add(copyI1);
        step1.add(Box.createVerticalStrut(10));
        step1.add(checkSSHRow);
        step1.add(Box.createVerticalStrut(10));
        step1.add(proceedI1);
        step1.add(Box.createVerticalStrut(20));

        // STEP 1a: DOWNLOAD SSH IF NOT DOWNLOADED
        JPanel step1a = new JPanel();
        step1a.setLayout(new BoxLayout(step1a, BoxLayout.Y_AXIS));
        step1a.add(new JLabel("Step 1a: Download SSH"));
        step1a.add(Box.createVerticalStrut(10));

        JTextArea download = util.buildTextArea(step1);
        download.setText("""
            If the terminal says anything along the lines of "Unit file sshd.service does not exist", then you do not have ssh installed. Run the following command in your terminal to install SSH:
            """);
        step1a.add(download);
        step1a.add(Box.createVerticalStrut(10));

        String downloadCmd = "sudo apt install openssh-server openssh-client";
        JPanel downloadSSHRow = util.buildCopyRow(downloadCmd);
        step1a.add(downloadSSHRow);
        step1a.add(Box.createVerticalStrut(10));

        JTextArea enable = util.buildTextArea(step1);
        enable.setText("""
            It may be the case that SSH is installed but is not active. To enable SSH, run the following command in your terminal:
            """);
        step1a.add(download);
        step1a.add(Box.createVerticalStrut(10));

        String enableCmd = "sudo systemctl start ssh";
        JPanel enableSSHRow = util.buildCopyRow(enableCmd);
        step1a.add(enableSSHRow);
        step1a.add(Box.createRigidArea(new Dimension(0, 20)));

        // STEP 1b: VERIFY SSH DOWNLOAD
        JPanel step1b = new JPanel();
        step1b.setLayout(new BoxLayout(step1b, BoxLayout.Y_AXIS));
        step1b.add(new JLabel("Step 1b: Verify that SSH was installed"));
        step1b.add(Box.createVerticalStrut(10));

        JTextArea copyI1b = util.buildTextArea(step1b);
        copyI1b.setText("Now that you have downloaded/enabled OpenSSH, verify whether or not you have ssh installed. run the following command again:");
        JPanel checkbSSHRow = util.buildCopyRow(sshCmd);

        JTextArea proceedI1b = util.buildTextArea(step1b);
        proceedI1b.setText("Now, there should be a line printed in the output of the terminal that says 'Active: active (running)'. proceed to [STEP 2].");

        step1b.add(copyI1b);
        step1b.add(Box.createVerticalStrut(10));
        step1b.add(checkbSSHRow);
        step1b.add(Box.createVerticalStrut(10));
        step1b.add(proceedI1b);
        step1b.add(Box.createVerticalStrut(20));

        inner.add(step1);
        inner.add(step1a);

        JScrollPane scroll = new JScrollPane(inner);

        panel.add(scroll);
        return panel;
    }

    JPanel buildSSH_Step2(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // STEP 2 GENERATE SSH KEY
        JPanel step2 = new JPanel();
        step2.setLayout(new BoxLayout(step2, BoxLayout.Y_AXIS));
        step2.add(new JLabel("Step 2: SSH Key"));
        step2.add(Box.createVerticalStrut(10));

        JTextArea copyI2 = util.buildTextArea(step2);
        copyI2.setText("Now that SSH is downloaded, you must establish yourself as a known host for the RPi. This will allow you to connect remotely to the RPi without having to enter a password. First, let's check to see if you already have an SSH key. Run the following command in your terminal:");

        String checkCmd = "ls -al ~/.ssh";
        JPanel checkKeySSHRow = util.buildCopyRow(checkCmd);

        JTextArea proceedI2 = util.buildTextArea(step2);
        proceedI2.setText("If there is a file called id_ed_25519, then you already have an SSH key. Proceed to [STEP 3]. If you do not have a file called id_ed_25519, proceed to [STEP 2a].");

        step2.add(copyI2);
        step2.add(Box.createVerticalStrut(10));
        step2.add(checkKeySSHRow);
        step2.add(Box.createVerticalStrut(10));
        step2.add(proceedI2);
        step2.add(Box.createVerticalStrut(20));

        // STEP 2 GENERATE SSH KEY
        JPanel step2a = new JPanel();
        step2a.setLayout(new BoxLayout(step2a, BoxLayout.Y_AXIS));
        step2a.add(new JLabel("Step 2a: Generate a SSH Key"));
        step2a.add(Box.createVerticalStrut(10));

        JTextArea copyI2a = util.buildTextArea(step2);
        copyI2a.setText("If the SSH key does not exist yet, we must generate one to be copied to the RPi. Run the following command into your terminal:");

        String generateCmd = "ssh-keygen -t ed25519";
        JPanel genSSHRow = util.buildCopyRow(generateCmd);

        step2a.add(copyI2a);
        step2a.add(Box.createVerticalStrut(10));
        step2a.add(genSSHRow);
        step2a.add(Box.createVerticalStrut(10));

        // Add to Panel
        inner.add(step2);
        inner.add(step2a);

        JScrollPane scroll = new JScrollPane(inner);

        panel.add(scroll);
        return panel;
    }

    JPanel buildSSH_Step3(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // STEP 3: COPY SSH KEY
        JPanel step3 = new JPanel();
        step3.setLayout(new BoxLayout(step3, BoxLayout.Y_AXIS));
        step3.add(new JLabel("Step 3: Copy SSH Key to Each Raspberry Pi"));
        step3.add(Box.createVerticalStrut(10));


        JTextArea copyI3 = util.buildTextArea(step3);
        copyI3.setText("Next, we must copy the key that you just generated over to the RPi. In the terminal, run the following command:");

        String copyCmd = "ssh-copy-id <rpi_name>@<rpi_addr>";
        JPanel copySSHRow = util.buildCopyRow(copyCmd);

        JTextArea changeI3 = util.buildTextArea(step3);
        changeI3.setText("Make sure to change <rpi_name> and <rpi_addr> with the correct information. You may need to input the RPi's password on this step.");

        step3.add(copyI3);
        step3.add(Box.createVerticalStrut(10));
        step3.add(copySSHRow);
        step3.add(Box.createVerticalStrut(10));
        step3.add(changeI3);
        step3.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add to Panel
        inner.add(step3);

        JScrollPane scroll = new JScrollPane(inner);

        panel.add(scroll);
        return panel;
    }

    JPanel buildSSH_Step4(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // STEP 4: VERIFY SSH CONNECTION
        JPanel step4 = new JPanel();
        step4.setLayout(new BoxLayout(step4, BoxLayout.Y_AXIS));
        step4.add(new JLabel("Step 4: Verify SSH Connection"));
        step4.add(Box.createVerticalStrut(10));


        JTextArea copyI4 = util.buildTextArea(step4);
        copyI4.setText("Finally, check to make sure that the SSH connection is working properly. In the terminal, run the following command:");

        String verifyCmd = "ssh <rpi_name>@<rpi_addr>";
        JPanel verifySSHRow = util.buildCopyRow(verifyCmd);

        JTextArea changeI4 = util.buildTextArea(step4);
        changeI4.setText("Make sure to change <rpi_name> and <rpi_addr> with the correct information. If you setup the ssh connection correctly, you should be able to access the RPi without having to input a password.");

        step4.add(copyI4);
        step4.add(Box.createVerticalStrut(10));
        step4.add(verifySSHRow);
        step4.add(Box.createVerticalStrut(10));
        step4.add(changeI4);
        step4.add(Box.createVerticalStrut(20));

        // STEP 4: VERIFY SSH CONNECTION
        JPanel step5 = new JPanel();
        step5.setLayout(new BoxLayout(step5, BoxLayout.Y_AXIS));
        step5.add(new JLabel("Step 5: Verify SSH Connection"));
        step5.add(Box.createVerticalStrut(10));


        JTextArea I5 = util.buildTextArea(step5);
        I5.setText("You have successfully set up the SSH connection for your RPi. Remember to repeat steps 1-4 of the SSH setup for each raspberry pi you have.");

        // Add to Panel
        inner.add(step4);
        inner.add(step5);

        JScrollPane scroll = new JScrollPane(inner);
        scroll.getViewport().setViewPosition(new Point(0, 0));

        panel.add(scroll);
        return panel;
    }
}
