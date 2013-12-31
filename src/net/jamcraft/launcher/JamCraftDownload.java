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
        sl_contentPane.putConstraint(SpringLayout.EAST, progressBar, -5, SpringLayout.EAST, contentPane);
        contentPane.add(progressBar);

        JLabel lblProgress = new JLabel("Progress: ");
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblProgress, 5, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.NORTH, progressBar, 6, SpringLayout.SOUTH, lblProgress);
        sl_contentPane.putConstraint(SpringLayout.WEST, progressBar, 0, SpringLayout.WEST, lblProgress);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblProgress, 10, SpringLayout.WEST, contentPane);
        contentPane.add(lblProgress);

        lblProgressPercent = new JLabel("");
        sl_contentPane.putConstraint(SpringLayout.WEST, lblProgressPercent, 6, SpringLayout.EAST, lblProgress);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblProgressPercent, -6, SpringLayout.NORTH, progressBar);
        contentPane.add(lblProgressPercent);

        textArea = new JTextArea();
        sl_contentPane.putConstraint(SpringLayout.NORTH, textArea, 9, SpringLayout.SOUTH, progressBar);
        sl_contentPane.putConstraint(SpringLayout.WEST, textArea, 12, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, textArea, -40, SpringLayout.SOUTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, textArea, -10, SpringLayout.EAST, contentPane);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        textArea.setFont(new Font("Lucida Console", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        contentPane.add(textArea);

        JButton btnCancel = new JButton("Cancel");
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnCancel, 6, SpringLayout.SOUTH, textArea);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.EAST, contentPane);
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
        contentPane.add(btnCancel);
    }

    public void update()
    {
        mainWorker = new SwingWorker<Void, Void>()
        {
            @Override
            public Void doInBackground()
            {
                cleanConfigFolder();
                removeModsFolder();
                removePreviousArchives();
                try
                {
                    downloadFile(new URL(Settings.jenkinsURL + "JamCraft-" + currVer + ".7z"), new File("./JamCraft-" + currVer + ".7z").getCanonicalFile());
                    extractArchive(new File("./JamCraft-" + currVer + ".7z").getCanonicalFile());
                    setVisible(false);
                    dispose();
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
        if (mainWorker != null && !mainWorker.isCancelled())
        {
            textArea.append("Cleaning mods folder...\n");
            File modsFolder = new File(Settings.mcDir + "mods/");
            if (modsFolder.listFiles() != null)
            {
                for (File f : modsFolder.listFiles())
                {
                    if (f.getName().contains(".jar"))
                    {
                        // f.delete();
                    }
                }
            }
        }
    }

    public void cleanConfigFolder()
    {
        if (mainWorker != null && !mainWorker.isCancelled())
        {
            textArea.append("Cleaning config folder...\n");
            try
            {
                File configFolder = new File(Settings.mcDir + "/config/").getCanonicalFile();
                if (configFolder.listFiles() != null)
                {
                    for (File f : configFolder.listFiles())
                    {
                        if (!Settings.doNotRemoveConfigs.contains(f.getName()))
                        {
                            // f.delete();
                            System.out.println(f.getName());
                        }
                    }
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void removePreviousArchives()
    {
        if (mainWorker != null && !mainWorker.isCancelled())
        {
            textArea.append("Removing previously downloaded packs...\n");
        }
    }

    public void extractArchive(File file) throws IOException
    {
        if (mainWorker != null && !mainWorker.isCancelled())
        {
            textArea.append("Extracting and installing latest modpack...\n");
            List<String> args = new ArrayList<String>();
            args.add(new File("./executables/7za.exe").getCanonicalFile().getAbsolutePath());
            args.add("x");
            args.add("-aoa");
            args.add(file.getAbsolutePath());
            ProcessBuilder proc = new ProcessBuilder(args);
            File dir = new File(Settings.mcDir).getCanonicalFile();
            if (!dir.exists())
            {
                dir.mkdir();
            }
            proc.directory(dir);
            if (mainWorker != null && !mainWorker.isCancelled())
            {
                Process p = proc.start();
            }
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
        if (mainWorker != null && !mainWorker.isCancelled())
        {
            textArea.append("Downloading newest pack...\n");
            if (currVer == 0)
            {
                getLatestVersionFromURL(new URL(Settings.jenkinsURL));
            }
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

    public int getCurrentVersion()
    {
        int highestVer = 0;
        try
        {

            File currDir = new File(".").getCanonicalFile();
            for (File f : currDir.listFiles())
            {
                if (f.getName().contains(".7z"))
                {
                    int start = f.getName().indexOf("JamCraft-") + 9;
                    int end = f.getName().indexOf(".7z");
                    if (f.getName().substring(start, end).matches("[0-9]+"))
                    {
                        int ver = Integer.parseInt(f.getName().substring(start, end));
                        if (highestVer < ver)
                        {
                            highestVer = ver;
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return highestVer;
    }
}
