package top.chorg.System;

import top.chorg.Support.Config;

import java.io.*;
import java.util.Formatter;

import static top.chorg.System.Global.glob;

public class Sys {
    private static String LogRoute = "logs/";
    private static String ConfigRoute = "";

    public static void warn(String sender, String msg) {
        String content = String.format("[%s] Warning: %s", sender, msg);
        if (isDevEnv() || isCmdEnv()) {
            System.out.println(content);
        } else {
            log(content);
        }
    }

    public static void warnF(String sender, String format, Object ... args) {
        warn(sender, new Formatter().format(format, args).toString());
    }

    public static void err(String sender, String msg) {
        String content = String.format("[%s] Error: %s", sender, msg);
        if (isDevEnv() || isCmdEnv()) {
            System.out.println(content);
        } else {
            log(content);
        }
    }

    public static void errF(String sender, String format, Object ... args) {
        err(sender, new Formatter().format(format, args).toString());
    }

    public static void info(String sender, String msg) {
        String content = String.format("[%s] DEV: %s", sender, msg);
        if (isDevEnv() || isCmdEnv()) {
            System.out.println(content);
        }
    }

    public static void infoF(String sender, String format, Object ... args) {
        DevInfo(sender, new Formatter().format(format, args).toString());
    }

    public static void DevInfo(String sender, String msg) {
        String content = String.format("[%s] DEV: %s", sender, msg);
        if (isDevEnv() || isCmdEnv()) {
            System.out.println(content);
        }
    }

    public static void DevInfoF(String sender, String format, Object ... args) {
        DevInfo(sender, new Formatter().format(format, args).toString());
    }

    // To decide if this is development environment.
    public static boolean isDevEnv() {
        return Global.DEV_MODE;
    }

    public static boolean isCmdEnv() {
        return !Global.GUI_MODE;
    }

    public static boolean isGuiEnv() {
        return Global.GUI_MODE;
    }

    // Exit the system.
    public static void exit(int returnValue) {
        if (isDevEnv()) {
            System.exit(returnValue);
        } else {
            System.out.printf("EXIT ERROR!(%d)\n", returnValue);
        }
    }


    public static void resetLogRoute(String logRoute) {
        System.out.println("RESET LOG ROUTE ERROR!");
    }

    public static void log(String msg) {
        System.out.printf("LOG INCOMPLETE!(%s)\n", msg);
    }

    public static Config loadConfig() {
        try {
            FileInputStream fileIn = new FileInputStream(ConfigRoute + "config.conf");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object savedObj = in.readObject();
            in.close();
            fileIn.close();
            if (savedObj instanceof Config) {
                return (Config) savedObj;
            } else {
                err("Config Loader", "Invalid conf file!");
                exit(6);
            }
        } catch(IOException i) {
            err("Config Loader", "Unable to open conf file for reading!");
            i.printStackTrace();
            exit(4);
        } catch(ClassNotFoundException c) {
            err("Config Loader", "Unexpected Error: Unable to load Class!");
            exit(5);
        }
        return null;
    }

    public static void reloadConfig() {
        glob.assignConfObj(loadConfig());
    }

    public static void saveConfig() {
        try {
            FileOutputStream fileOut = new FileOutputStream(ConfigRoute + "config.conf");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(glob.getConfObj());
            out.close();
            fileOut.close();
            DevInfo("Config Saver", "Serialized data is saved in config.conf.");
        } catch(IOException i) {
            err("Config Saver", "Unable to open conf file for saving!");
            i.printStackTrace();
            exit(3);
        }
    }

    public static void clearConfig() {
        glob.clearConfig();
    }
}
