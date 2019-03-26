package top.chorg.System;

import top.chorg.Support.SerializableMap;

import java.io.*;
import java.util.HashMap;

import static top.chorg.System.Sys.*;

public class Global {
    private static HashMap<String, Object> variables = new HashMap<>();
    private static SerializableMap configs;

    public static void setVar(String key, Object value) {
        if (variables.containsKey(key)) {
            String ori = variables.get(key).toString();
            variables.replace(key, value);
            Sys.DevInfoF("Global","Global '%s' replaced ('%s' -> '%s').", key, ori, value.toString());
        } else {
            variables.put(key, value);
        }
    }

    public static Object getVar(String key) {
        if (!variables.containsKey(key)) {
            System.out.println(key);
            Sys.errF("Global", "Global '%s' not exist.", key);
            exit(1);
        }
        return variables.get(key);
    }

    public static boolean varExists(String key) {
        return variables.containsKey(key);
    }

    public static void dropVar(String key) {
        if (varExists(key)) variables.remove(key);
    }

    public static void setConfig(String key, Serializable value) {
        if (!configs.containsKey(key)) {
            if (Sys.isDevEnv()) {
                configs.put(key, value);
                Sys.DevInfoF("Config", "Config '%s' have been added to the set.", key);
            } else {
                Sys.errF("Config", "Invalid config key '%s'.", key);
                exit(2);
            }
        } else {
            configs.replace(key, value);
        }
    }

    public static Serializable getConfig(String key) {
        if (!configs.containsKey(key)) {
            Sys.errF("Config", "Invalid config key '%s'.", key);
            exit(2);
        }
        return configs.get(key);
    }

    public static void assignConfObj(SerializableMap conf) {
        configs = conf;
    }

    public static SerializableMap getConfObj() {
        return configs;
    }

    public static void clearConfig() {
        if (!Sys.isDevEnv()) {
            err("System", "Unauthorized operation (clear config)!");
            exit(201);
        }
        configs = new SerializableMap();
    }

    public static SerializableMap readConfig() {
        try {
            FileInputStream fileIn = new FileInputStream(getVar("CONF_ROUTE") + "config.conf");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object savedObj = in.readObject();
            in.close();
            fileIn.close();
            if (savedObj instanceof SerializableMap) {
                return (SerializableMap) savedObj;
            } else {
                err("SerializableMap Loader", "Invalid conf file!");
                exit(6);
            }
        } catch(IOException i) {
            err("SerializableMap Loader", "Unable to open conf file for reading!");
            i.printStackTrace();
            exit(4);
        } catch(ClassNotFoundException c) {
            err("SerializableMap Loader", "Unexpected Error: Unable to load Class!");
            exit(5);
        }
        return null;
    }

    public static void reloadConfig() {
        Global.assignConfObj(readConfig());
    }

    public static void saveConfig() {
        try {
            FileOutputStream fileOut = new FileOutputStream(getVar("CONF_ROUTE") + "config.conf");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(Global.getConfObj());
            out.close();
            fileOut.close();
            DevInfo("SerializableMap Saver", "Serialized data is saved in config.conf.");
        } catch(IOException i) {
            err("SerializableMap Saver", "Unable to open conf file for saving!");
            i.printStackTrace();
            exit(3);
        }
    }
}
