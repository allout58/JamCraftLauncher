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
import javax.swing.SwingWorker;

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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JamCraftDownload extends JFrame
{

    private JPanel contentPane;
    private JProgressBar progressBar;
    private JLabel lblProgressPercent;
    public JTextArea textArea;

    public int currVer = 0;

    private SwingWorker<Void, Void> mainWorker;

    /**
     * Create the frame.
     */
    public JamCraftDownload()
    {
        super();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        SpringLayout sl_contentPane = new SpringLayout();
        contentPane.setLayout(sl_contentPane);

        progressBar = new JProgressBar();
        sl_contentPane.putConstraint(SpringLayout.NORTH, progressBar, 32, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, progressBar, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, progressBar, -43, SpringLayout.EAST, contentPane);
        contentPane.add(progressBar);

        JLabel lblProgress = new JLabel("Progress: ");
        sl_contentPane.putConstraint(SpringLayout.WEST, lblProgress, 0, SpringLayout.WEST, progressBar);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblProgress, -6, SpringLayout.NORTH, progressBar);
        contentPane.add(lblProgress);

        lblProgressPercent = new JLabel("");
        sl_contentPane.putConstraint(SpringLayout.WEST, lblProgressPercent, 6, SpringLayout.EAST, lblProgress);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblProgressPercent, -6, SpringLayout.NORTH, progressBar);
        contentPane.add(lblProgressPercent);

        textArea = new JTextArea();
        sl_contentPane.putConstraint(SpringLayout.SOUTH, textArea, -40, SpringLayout.SOUTH, contentPane);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        textArea.setFont(new Font("Lucida Console", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        sl_contentPane.putConstraint(SpringLayout.NORTH, textArea, 9, SpringLayout.SOUTH, progressBar);
        sl_contentPane.putConstraint(SpringLayout.WEST, textArea, 2, SpringLayout.WEST, progressBar);
        sl_contentPane.putConstraint(SpringLayout.EAST, textArea, 312, SpringLayout.EAST, lblProgress);
        contentPane.add(textArea);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                textArea.append("Cancelling...\n");
                mainWorker.cancel(true);
                setVisible(false);
                dispose();
            }
        });
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnCancel, 6, SpringLayout.SOUTH, textArea);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.EAST, contentPane);
        contentPane.add(btnCancel);
    }

    public void update()
    {
        mainWorker = new SwingWorker<Void, Void>()
        {
            @Override
            public Void doInBackground()
            {
                if (!isCancelled())
                {
                    textArea.append("Cleaning config folder...\n");
                    cleanConfigFolder();
                }
                if (!isCancelled())
                {
                    textArea.append("Cleaning mods folder...\n");
                    removeModsFolder();
                }
                if (!isCancelled())
                {
                    textArea.append("Removing previously downloaded packs...\n");
                    removePreviousArchives();
                }
                try
                {
                    if (!isCancelled())
                    {
                        textArea.append("Downloading newest pack...\n");
                        downloadFile(new URL(Settings.jenkinsURL + "JamCraft-" + currVer + ".7z"), new File("./JamCraft-" + currVer + ".7z").getCanonicalFile());
                    }
                    if (!isCancelled())
                    {
                        textArea.append("Extracting and installing latest modpack...\n");
                        extractArchive(new File("./JamCraft-" + currVer + ".7z").getCanonicalFile());
                    }
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };
        mainWorker.execute();
    }

    public void removeModsFolder()
    {
        File modsFolder = new File(Settings.mcDir + "mods/");
    }

    public void cleanConfigFolder()
    {

    }

    public void removePreviousArchives()
    {

    }

    public void extractArchive(File file) throws IOException
    {
        List<String> args = new ArrayList<String>();
        args.add(new File("./executables/7za.exe").getCanonicalFile().getAbsolutePath());
        args.add("x");
        args.add("-aoa");
        args.add(file.getAbsolutePath());
        ProcessBuilder proc = new ProcessBuilder(args);
        File dir=new File(Settings.mcDir).getCanonicalFile();
        if(!dir.exists())
        {
            dir.mkdir();
        }
        proc.directory(dir);
        if (mainWorker != null && !mainWorker.isCancelled())
        {
            Process p = proc.start();
        }
    }

    public int getLatestVersionFromURL(URL url) throws IOException
    {
        int version = 0;
        URLConnection con = url.openConnection();
        InputStreamReader s = new InputStreamReader(con.getInputStream());
        BufferedReader br = new BufferedReader(s);
        String readLine;
        String foundURL = "";

        while ((readLine = br.readLine()) != null)
        {
            if (readLine.contains(".7z"))
            {
                int startURL = readLine.indexOf("href=\"Jam") + 5;
                int endURL = readLine.indexOf(".7z") + 3;
                foundURL = readLine.substring(startURL, endURL);
            }
        }

        if (foundURL != "")
        {
            String vString = foundURL.substring(foundURL.indexOf("-") + 1, foundURL.indexOf(".7z"));
            version = Integer.parseInt(vString);
        }
        this.currVer = version;
        return version;
    }

    public void downloadFile(final URL downloadURL, final File pathToDownload) throws IOException
    {
        final int blk_size = 8192;
        try
        {
            URLConnection con = downloadURL.openConnection();
            InputStream reader = downloadURL.openStream();
            FileOutputStream writer = new FileOutputStream(pathToDownload);
            int total = con.getContentLength();
            int size_dl = 0;
            byte[] buffer = new byte[blk_size];
            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer)) > 0 && !mainWorker.isCancelled())
            {
                size_dl += bytesRead;
                // System.out.println((size_dl / total * 100) + "%");
                progressBar.setString((size_dl / total * 100) + "%");
                progressBar.setValue(size_dl / total * 100);
                lblProgressPercent.setText(size_dl + "B out of " + total + "B");
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[blk_size];
            }
            writer.close();
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
