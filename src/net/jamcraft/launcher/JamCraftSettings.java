package net.jamcraft.launcher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import net.ftb.util.OSUtils;
import net.ftb.util.OSUtils.OS;
import net.ftb.util.winreg.JavaFinder;

public class JamCraftSettings extends JDialog
{

    private final JPanel contentPanel = new JPanel();
    private JTextField textFieldJamCraftJenkins;
    public JSlider ramMaximum;
    public JLabel currentRam;
    private JTextField textFieldInstallDir;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        try
        {
            JamCraftSettings dialog = new JamCraftSettings();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public JamCraftSettings()
    {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        SpringLayout sl_contentPanel = new SpringLayout();
        contentPanel.setLayout(sl_contentPanel);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        sl_contentPanel.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.NORTH, contentPanel);
        sl_contentPanel.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, contentPanel);
        sl_contentPanel.putConstraint(SpringLayout.SOUTH, tabbedPane, 0, SpringLayout.SOUTH, contentPanel);
        sl_contentPanel.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST, contentPanel);
        contentPanel.add(tabbedPane);

        JPanel panelMCSettings = new JPanel();
        SpringLayout sl_panelMCSettings = new SpringLayout();
        panelMCSettings.setLayout(sl_panelMCSettings);
        tabbedPane.addTab("Minecraft Settings", null, panelMCSettings, null);

        currentRam = new JLabel();
        currentRam.setBounds(427, 95, 85, 25);
        long ram = 0;
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        Method m;
        try
        {
            m = operatingSystemMXBean.getClass().getDeclaredMethod("getTotalPhysicalMemorySize");
            m.setAccessible(true);
            Object value = m.invoke(operatingSystemMXBean);
            if (value != null)
            {
                ram = Long.valueOf(value.toString()) / 1024 / 1024;
            }
            else
            {
                ram = 8192;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Taken from the FTB launcher; modified slightly
        ramMaximum = new JSlider();
        sl_panelMCSettings.putConstraint(SpringLayout.NORTH, currentRam, 5, SpringLayout.NORTH, ramMaximum);
        sl_panelMCSettings.putConstraint(SpringLayout.WEST, currentRam, 6, SpringLayout.EAST, ramMaximum);
        sl_panelMCSettings.putConstraint(SpringLayout.NORTH, ramMaximum, 52, SpringLayout.NORTH, panelMCSettings);
        sl_panelMCSettings.putConstraint(SpringLayout.WEST, ramMaximum, 59, SpringLayout.WEST, panelMCSettings);
        ramMaximum.setBounds(190, 95, 222, 25);
        ramMaximum.setSnapToTicks(true);
        ramMaximum.setMajorTickSpacing(256);
        ramMaximum.setMinorTickSpacing(256);
        ramMaximum.setMinimum(256);
        String vmType = new String();
        if (OSUtils.getCurrentOS().equals(OS.WINDOWS))
        {
            vmType = JavaFinder.parseWinJavaVersion().is64bits ? "64" : "32";
        }
        else
        {
            vmType = System.getProperty("sun.arch.data.model");
        }
        if (vmType != null)
        {
            if (vmType.equals("64"))
            {
                ramMaximum.setMaximum((int) ram);
            }
            else if (vmType.equals("32"))
            {
                if (ram < 1024)
                {
                    ramMaximum.setMaximum((int) ram);
                }
                else
                {
                    ramMaximum.setMaximum(1024);
                }
            }
        }
        int ramMax = (Settings.memoryMB > ramMaximum.getMaximum()) ? ramMaximum.getMaximum() : Settings.memoryMB;
        ramMaximum.setValue(ramMax);
        currentRam.setText(getAmount());
        ramMaximum.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent arg0)
            {
                currentRam.setText(getAmount());
            }
        });
        panelMCSettings.add(ramMaximum);
        panelMCSettings.add(currentRam);
        // end taken from FTB Launcher

        JLabel lblRam = new JLabel("RAM:");
        sl_panelMCSettings.putConstraint(SpringLayout.NORTH, lblRam, 5, SpringLayout.NORTH, ramMaximum);
        sl_panelMCSettings.putConstraint(SpringLayout.EAST, lblRam, -9, SpringLayout.WEST, ramMaximum);
        panelMCSettings.add(lblRam);

        JLabel lblInstallDir = new JLabel("Install Dir:");
        sl_panelMCSettings.putConstraint(SpringLayout.NORTH, lblInstallDir, 10, SpringLayout.NORTH, panelMCSettings);
        sl_panelMCSettings.putConstraint(SpringLayout.WEST, lblInstallDir, 10, SpringLayout.WEST, panelMCSettings);
        panelMCSettings.add(lblInstallDir);

        textFieldInstallDir = new JTextField();
        textFieldInstallDir.setToolTipText("Where you want your JamCraft instance to be installed to.");
        sl_panelMCSettings.putConstraint(SpringLayout.NORTH, textFieldInstallDir, 10, SpringLayout.NORTH, panelMCSettings);
        sl_panelMCSettings.putConstraint(SpringLayout.WEST, textFieldInstallDir, 6, SpringLayout.EAST, lblInstallDir);
        sl_panelMCSettings.putConstraint(SpringLayout.EAST, textFieldInstallDir, 213, SpringLayout.EAST, lblInstallDir);
        try
        {
            textFieldInstallDir.setText(new File(Settings.mcDir).getCanonicalPath().toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        panelMCSettings.add(textFieldInstallDir);
        textFieldInstallDir.setColumns(10);

        JButton btnBrowse = new JButton("Browse...");
        btnBrowse.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(Settings.mcDir));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(currentRam) == JFileChooser.APPROVE_OPTION)
                {
                    textFieldInstallDir.setText(chooser.getSelectedFile().getPath());
                }
            }
        });
        sl_panelMCSettings.putConstraint(SpringLayout.NORTH, btnBrowse, 0, SpringLayout.NORTH, lblInstallDir);
        sl_panelMCSettings.putConstraint(SpringLayout.WEST, btnBrowse, 14, SpringLayout.EAST, textFieldInstallDir);
        panelMCSettings.add(btnBrowse);

        JPanel panelJamCraftSettings = new JPanel();
        SpringLayout sl_panelJamCraftSettings = new SpringLayout();
        panelJamCraftSettings.setLayout(sl_panelJamCraftSettings);
        tabbedPane.addTab("JamCraft Settings", null, panelJamCraftSettings, null);

        textFieldJamCraftJenkins = new JTextField();
        textFieldJamCraftJenkins.setText(Settings.jenkinsURL);
        sl_panelJamCraftSettings.putConstraint(SpringLayout.NORTH, textFieldJamCraftJenkins, 10, SpringLayout.NORTH, panelJamCraftSettings);
        sl_panelJamCraftSettings.putConstraint(SpringLayout.WEST, textFieldJamCraftJenkins, 83, SpringLayout.WEST, panelJamCraftSettings);
        sl_panelJamCraftSettings.putConstraint(SpringLayout.EAST, textFieldJamCraftJenkins, 359, SpringLayout.WEST, panelJamCraftSettings);
        panelJamCraftSettings.add(textFieldJamCraftJenkins);
        textFieldJamCraftJenkins.setColumns(10);

        JLabel lblJenkinsURL = new JLabel("Jenkins URL");
        sl_panelJamCraftSettings.putConstraint(SpringLayout.NORTH, lblJenkinsURL, 13, SpringLayout.NORTH, panelJamCraftSettings);
        sl_panelJamCraftSettings.putConstraint(SpringLayout.EAST, lblJenkinsURL, -6, SpringLayout.WEST, textFieldJamCraftJenkins);
        lblJenkinsURL.setToolTipText("URL for the JamCraft Jenkins Server. Do not change unless you know what you are doing");
        lblJenkinsURL.setLabelFor(textFieldJamCraftJenkins);
        panelJamCraftSettings.add(lblJenkinsURL);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("Save");
                okButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent arg0)
                    {
                        Settings.jenkinsURL = textFieldJamCraftJenkins.getText();
                        Settings.mcDir = textFieldInstallDir.getText();
                        Settings.memoryMB = ramMaximum.getValue();
                        Settings.save();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent arg0)
                    {
                        setVisible(false);
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    //Taken from FTB Launcher
    private String getAmount()
    {
        int ramMax = ramMaximum.getValue();
        return (ramMax >= 1024) ? Math.round((ramMax / 256) / 4) + "." + (((ramMax / 256) % 4) * 25) + " GB" : ramMax + " MB";
    }
    //End taken from FTB Launcher
}
