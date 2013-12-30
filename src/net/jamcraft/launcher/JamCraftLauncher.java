package net.jamcraft.launcher;

import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import net.miginfocom.swing.MigLayout;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;

import javax.swing.SpringLayout;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class JamCraftLauncher
{

    private JFrame frame;
    private JPasswordField passwordField;
    private JTextField textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    JamCraftLauncher window = new JamCraftLauncher();
                    window.frame.setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public JamCraftLauncher()
    {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        frame.getContentPane().setLayout(springLayout);
        frame.setBounds(new Rectangle(700, 500));

        JButton btnLaunch = new JButton("Launch!");
        btnLaunch.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                File file = new File("C:\\Users\\jamesthollowell\\AppData\\Roaming\\.minecraft\\JamCraft\\JamCraft-167.7z");
                if (!file.exists())
                {
                    System.out.println("Error Reading file!");
                }
                else
                {
                    JamCraftDownload downloadWin = new JamCraftDownload();
                    try
                    {
//                        downloadWin.extractArchive(file);
                        int ver=downloadWin.getLatestVersionFromURL(new URL(Settings.jenkinsURL));
                        downloadWin.setVisible(true);
                        downloadWin.update();
//                        downloadWin.downloadFile(new URL(Settings.jenkinsURL+"JamCraft-"+ver+".7z"), new File("./JamCraft-"+ver+".7z").getCanonicalFile());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        springLayout.putConstraint(SpringLayout.SOUTH, btnLaunch, -10, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnLaunch, -10, SpringLayout.EAST, frame.getContentPane());
        frame.getContentPane().add(btnLaunch);

        passwordField = new JPasswordField();
        springLayout.putConstraint(SpringLayout.WEST, passwordField, -136, SpringLayout.WEST, btnLaunch);
        springLayout.putConstraint(SpringLayout.SOUTH, passwordField, -12, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, passwordField, -6, SpringLayout.WEST, btnLaunch);
        frame.getContentPane().add(passwordField);

        JLabel lblPassword = new JLabel("Password:");
        springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 4, SpringLayout.NORTH, btnLaunch);
        springLayout.putConstraint(SpringLayout.EAST, lblPassword, -6, SpringLayout.WEST, passwordField);
        frame.getContentPane().add(lblPassword);

        textField = new JTextField();
        springLayout.putConstraint(SpringLayout.SOUTH, textField, 0, SpringLayout.SOUTH, btnLaunch);
        springLayout.putConstraint(SpringLayout.EAST, textField, -6, SpringLayout.WEST, lblPassword);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JLabel lblUsername = new JLabel("Username:");
        springLayout.putConstraint(SpringLayout.NORTH, lblUsername, 3, SpringLayout.NORTH, textField);
        springLayout.putConstraint(SpringLayout.EAST, lblUsername, -6, SpringLayout.WEST, textField);
        frame.getContentPane().add(lblUsername);

        JTextPane textPaneInfo = new JTextPane();
        textPaneInfo.setEditable(false);
        springLayout.putConstraint(SpringLayout.NORTH, textPaneInfo, 30, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, textPaneInfo, 10, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, textPaneInfo, -22, SpringLayout.NORTH, passwordField);
        springLayout.putConstraint(SpringLayout.EAST, textPaneInfo, 457, SpringLayout.WEST, frame.getContentPane());
        frame.getContentPane().add(textPaneInfo);
    }
}
