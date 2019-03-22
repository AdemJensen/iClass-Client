package top.chorg.System;

import java.io.Serializable;
import java.util.HashMap;

import top.chorg.Support.Config;

public class Var {
    private HashMap<String, Object> variables = new HashMap<>();
    private Config configs = Sys.loadConfig();

    public Var() {

    }

    public void setVar(String key, Object value) {
        if (variables.containsKey(key)) {
            String ori = variables.get(key).toString();
            variables.replace(key, value);
            Sys.warnF("Global","Global '%s' replaced ('%s' -> '%s').", key, ori, value.toString());
        } else {
            variables.put(key, value);
        }
    }

    public Object getVar(String key) {
        if (!variables.containsKey(key)) {
            Sys.errF("Global", "Global '%s' not exist.", key);
            Sys.exit(1);
        }
        return variables.get(key);
    }

    public void setConfig(String key, Serializable value) {
        if (!configs.containsKey(key)) {
            if (Sys.isDevEnv()) {
                configs.put(key, value);
                Sys.DevInfoF("Config", "Config '%s' have been added to the set.", key);
            } else {
                Sys.errF("Config", "Invalid config key '%s'.", key);
                Sys.exit(2);
            }
        } else {
            configs.replace(key, value);
        }
    }

    public Serializable getConfig(String key) {
        if (!configs.containsKey(key)) {
            Sys.errF("Config", "Invalid config key '%s'.", key);
            Sys.exit(2);
        }
        return configs.get(key);
    }

    public void assignConfObj(Config conf) {
        configs = conf;
    }

    public Config getConfObj() {
        return configs;
    }

    public void clearConfig() {
        if (!Sys.isDevEnv()) {
            Sys.err("System", "Unauthorized operation (clear config)!");
            Sys.exit(201);
        }
    }
}
