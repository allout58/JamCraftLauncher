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
    
    public void extractArchive(File file) throws SevenZipException, IOException
    {
        RandomAccessFile raf=new RandomAccessFile(file, "r");
        final ISevenZipInArchive inArchive=SevenZip.openInArchive(null, new RandomAccessFileInStream(raf));
        inArchive.extract(null, false, new IArchiveExtractCallback()
        {
            long total=0;
            @Override
            public void setTotal(long arg0) throws SevenZipException
            {
                total=arg0;
            }
            
            @Override
            public void setCompleted(long arg0) throws SevenZipException
            {
                System.out.println((double)arg0/total);               
            }
            
            @Override
            public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException
            {
                // TODO Auto-generated method stub
                
            }
            
            public ISequentialOutStream getStream(final int index, ExtractAskMode extractAskMode) throws SevenZipException {
                return new ISequentialOutStream() {
                @Override
                public int write(byte[] data) throws SevenZipException {
                    String filePath = inArchive.getStringProperty(index, PropID.PATH);
                    FileOutputStream fos = null;
                    try {
                    File dir = new File("C:/Jamcraft/");
                    File path = new File("C:/Jamcraft/" + filePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (!path.exists()) {
                        path.createNewFile();
                    }
                    fos = new FileOutputStream(path, true);
                    fos.write(data);
                    } catch (IOException e) {
//                    logger.severe(e.getLocalizedMessage());
                    } finally {
                    try {
                        if (fos != null) {
                        fos.flush();
                        fos.close();
                        }
                    } catch (IOException e) {
//                        logger.severe(e.getLocalizedMessage());
                    }
                    }
                    return data.length;
                }
                };
            }
        }); 
        raf.close();
    }
}
