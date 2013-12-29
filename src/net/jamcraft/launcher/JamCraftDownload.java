package net.jamcraft.launcher;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class JamCraftDownload extends JFrame
{

    private JPanel contentPane;
    
    private int totalExtract=0;
    private int currentExtract=0;


    /**
     * Create the frame.
     */
    public JamCraftDownload()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        SpringLayout sl_contentPane = new SpringLayout();
        contentPane.setLayout(sl_contentPane);
        
        JProgressBar progressBar = new JProgressBar();
        sl_contentPane.putConstraint(SpringLayout.NORTH, progressBar, 32, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, progressBar, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, progressBar, -43, SpringLayout.EAST, contentPane);
        contentPane.add(progressBar);
        
        JLabel lblProgress = new JLabel("Progress: ");
        sl_contentPane.putConstraint(SpringLayout.WEST, lblProgress, 0, SpringLayout.WEST, progressBar);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblProgress, -6, SpringLayout.NORTH, progressBar);
        contentPane.add(lblProgress);
        
        JLabel lblProgressPercent = new JLabel("");
        sl_contentPane.putConstraint(SpringLayout.WEST, lblProgressPercent, 6, SpringLayout.EAST, lblProgress);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblProgressPercent, -6, SpringLayout.NORTH, progressBar);
        contentPane.add(lblProgressPercent);
        
        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        textArea.setFont(new Font("Lucida Console", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        sl_contentPane.putConstraint(SpringLayout.NORTH, textArea, 9, SpringLayout.SOUTH, progressBar);
        sl_contentPane.putConstraint(SpringLayout.WEST, textArea, 2, SpringLayout.WEST, progressBar);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, textArea, -15, SpringLayout.SOUTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, textArea, 312, SpringLayout.EAST, lblProgress);
        contentPane.add(textArea);
    }
    
    public void extractArchive(File file) throws IOException
    {
        List<String> args=new ArrayList<String>();
        args.add(new File("./executables/7za.exe").getCanonicalFile().getAbsolutePath());
        args.add("x");
        args.add("-aoa");
        args.add(file.getAbsolutePath());
        ProcessBuilder proc=new ProcessBuilder(args);
        proc.directory(new File(".").getCanonicalFile());
        Process p=proc.start();
    }
}
