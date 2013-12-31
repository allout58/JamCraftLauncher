package net.jamcraft.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Settings
{
    private static final String propsLoc = "./settings.properties";
    private static final String dnrConfigLoc="./donotremove.properties";

    public static String mcDir = System.getenv("APPDATA")+"/.jamcraft/";
    public static int currentVersion = 0;
    public static String jenkinsURL = "http://jenkins.antichthon.net/jenkins/job/JamCraft%20Up2Date/lastSuccessfulBuild/artifact/";
    public static int memoryMB = 1024;// Default to 1GB
    public static String extraJava = "";
    
    public static List<String> doNotRemoveConfigs;

    private static void loadDNR()
    {
        doNotRemoveConfigs=new ArrayList<String>();
        doNotRemoveConfigs.add("InvTweaks.cfg");
        doNotRemoveConfigs.add("InvTweaksRules.txt");
        doNotRemoveConfigs.add("InvTweaksTree.txt");
        doNotRemoveConfigs.add("NEI.cfg");
        doNotRemoveConfigs.add("NEIServer.cfg");
        doNotRemoveConfigs.add("NEISubset.cfg");
    }
    
    public static void save()
    {
        Properties props = new Properties();
        try
        {
            File propFile = new File(Settings.propsLoc).getCanonicalFile();
            props.setProperty("mcDir", mcDir);
            props.setProperty("jenkinsURL", jenkinsURL);
            props.setProperty("memoryMB", String.valueOf(memoryMB));
            props.setProperty("extraJava", extraJava);
            props.store(new FileOutputStream(propFile), "JamCraft Launcher Settings");
            
            File dnrConfigFile=new File(Settings.dnrConfigLoc).getCanonicalFile();
            FileWriter fw=new FileWriter(dnrConfigFile);
            if(doNotRemoveConfigs==null)loadDNR();
            for (String config : doNotRemoveConfigs)
            {
                fw.write(config+"\n");
            }
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void load()
    {
        Properties props = new Properties();
        try
        {
            File propFile = new File(Settings.propsLoc).getCanonicalFile();
            props.load(new FileInputStream(propFile));
            mcDir = props.getProperty("mcDir");
            jenkinsURL = props.getProperty("jenkinsURL");
            String mem = props.getProperty("memoryMB");
            if (mem.matches("[0-9]+"))
            {
                memoryMB = Integer.parseInt(mem);
            }
            extraJava = props.getProperty("extraJava");
            
            File dnrConfigFile=new File(Settings.dnrConfigLoc).getCanonicalFile();
            BufferedReader fr=new BufferedReader(new FileReader(dnrConfigFile));
            String line;
            if(doNotRemoveConfigs==null)doNotRemoveConfigs=new ArrayList<String>();
            while((line = fr.readLine())!=null)
            {
                doNotRemoveConfigs.add(line);
            }
            fr.close();
        }
        catch (FileNotFoundException e)
        {
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
