package top.chorg.System;

import top.chorg.Kernel.Cmd.CmdManager;
import top.chorg.Kernel.Communication.Net.NetReceiver;
import top.chorg.Kernel.Communication.Net.NetSender;
import top.chorg.Support.SerializableMap;

import java.io.*;
import java.util.HashMap;

import static top.chorg.System.Sys.*;

/**
 * Contains all the global variables and configuration variables.
 *
 * <p>Fully static, no need to make instance.
 *
 * <p><strong>Attention: Method {@code reloadConfig()} or {@code assignConfig(SerializableMap)} must be run at lease
 * once to enable the configuration system.</strong>
 *
 * <ul>
 * <li>If you want to read configurations from the conf file, just use {@code reloadConfig()}.</li>
 * <li>If you want to make a new config variable container, use {@code assignConfig(new SerializableMap())}.</li>
 * </ul>
 */
public class Global {
    public static CmdManager cmdManPublic = new CmdManager();
    public static CmdManager cmdManPrivate = new CmdManager();

    public static NetSender masterSender;
    public static NetReceiver masterReceiver;

    private static HashMap<String, Object> variables = new HashMap<>();     // Contains global variables.
    private static SerializableMap configs;     // Contains configuration variables.

    /**
     * Modify or add a value in the global variable field.
     *
     * @param key Key of targeted global variable.
     * @param value Value of targeted global variable.
     */
    public static void setVar(String key, Object value) {
        if (variables.containsKey(key)) {
            String ori = variables.get(key).toString();
            variables.replace(key, value);
            Sys.devInfoF("Global","Global '%s' replaced ('%s' -> '%s').", key, ori, value.toString());
        } else {
            variables.put(key, value);
        }
    }

    /**
     * Get variable from global variable field.
     *
     * @param key Key of targeted global variable.
     * @return Value of targeted global variable.
     */
    public static Object getVar(String key) {
        if (!variables.containsKey(key)) {
            return null;
        }
        return variables.get(key);
    }

    /**
     * To tell if a global variable exists in the global variable field.
     *
     * @param key Key of targeted global variable.
     * @return True if the global variable exists in the global variable field, false if not.
     */
    public static boolean varExists(String key) {
        return variables.containsKey(key);
    }

    /**
     * Delete a global variable.
     *
     * @param key Key of targeted global variable.
     */
    public static void dropVar(String key) {
        if (varExists(key)) variables.remove(key);
    }

    /**
     * Set the value of a config variable.
     * Value must be serializable in order to be saved to a conf file.
     *
     * @param key Key of targeted global variable.
     * @param value Value of targeted global variable.
     */
    public static void setConfig(String key, Serializable value) {
        if (!configs.containsKey(key)) {
            if (Sys.isDevEnv()) {
                configs.put(key, value);
                Sys.devInfoF("Config", "Config '%s' have been added to the set.", key);
            } else {
                Sys.errF("Config", "Invalid config key '%s'.", key);
                exit(2);
            }
        } else {
            configs.replace(key, value);
        }
    }

    /**
     * Get the value of a config variable.
     *
     * @param key Key of targeted global variable.
     * @return Value of targeted global variable.
     */
    public static Serializable getConfig(String key) {
        if (!configs.containsKey(key)) {
            Sys.errF("Config", "Invalid config key '%s'.", key);
            exit(2);
        }
        return configs.get(key);
    }

    /**
     * Assign a configuration object into the config field.
     *
     * @param conf Targeted conf object.
     */
    public static void assignConfObj(SerializableMap conf) {
        configs = conf;
    }

    /**
     * Return the conf object stored in the Global field.
     *
     * @return Global conf object.
     */
    public static SerializableMap getConfObj() {
        return configs;
    }

    /**
     * Clear the config obj and make a new one.
     * Notice that this action is not allowed outside Development mode.
     */
    public static void clearConfig() {
        if (!Sys.isDevEnv()) {
            err("System", "Unauthorized operation (clear config)!");
            exit(201);
        }
        configs = new SerializableMap();
    }

    /**
     * Read config object from conf file.
     *
     * @param confFileName Full name of config file.
     * @return The config obj read from file.
     */
    public static SerializableMap readConfig(String confFileName) {
        try {
            FileInputStream fileIn = new FileInputStream(getVar("CONF_ROUTE") + "/" + confFileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object savedObj = in.readObject();
            in.close();
            fileIn.close();
            if (savedObj instanceof SerializableMap) {
                return (SerializableMap) savedObj;
            } else {
                err("SerializableMap Loader", "Invalid conf file!");
            }
        } catch(IOException i) {
            errF("SerializableMap Loader", "Unable to open conf file for reading (%s)!", confFileName);
        } catch(ClassNotFoundException c) {
            errF("SerializableMap Loader", "Unexpected Error: Unable to load Class (%s)!", confFileName);
        }
        return null;
    }

    public static boolean loadFileConfig() {
        SerializableMap config = readConfig((String) getVar("CONF_FILE"));
        if (config == null) return false;
        assignConfObj(config);
        return true;
    }

    public static boolean loadDefaultConfig() {
        SerializableMap config = readConfig((String) getVar("DEFAULT_CONF_FILE"));
        if (config == null) return false;
        assignConfObj(config);
        return true;
    }

    public static boolean saveDefaultConfig() {
        return saveConfigToFile((String) getVar("DEFAULT_CONF_FILE"));
    }

    public static boolean saveFileConfig() {
        return saveConfigToFile((String) getVar("CONF_FILE"));
    }

    public static boolean saveConfigToFile(String confFileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(getVar("CONF_ROUTE") + "/" + confFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(Global.getConfObj());
            out.close();
            fileOut.close();
            devInfoF("SerializableMap Saver", "Serialized data is saved in '%s'.", confFileName);
        } catch(IOException i) {
            errF("SerializableMap Saver", "Unable to open conf file for saving (%s)!", confFileName);
            return false;
        }
        return true;
    }
}
