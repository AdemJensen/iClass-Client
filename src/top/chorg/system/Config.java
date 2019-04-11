package top.chorg.system;

import java.io.*;

public class Config {
    public String Cmd_Server_Host;
    public int Cmd_Server_Port;
    public String File_Server_Host;
    public int File_Server_Port;

    public void assign(Config c) {
        Cmd_Server_Host = c.Cmd_Server_Host;
        Cmd_Server_Port = c.Cmd_Server_Port;
        File_Server_Host = c.File_Server_Host;
        File_Server_Port = c.File_Server_Port;
    }

    public boolean load() {
        return loadFromFile(Global.getVarCon("CONF_FILE", String.class));
    }

    public boolean loadDefault() {
        return loadFromFile(Global.getVarCon("DEFAULT_CONF_FILE", String.class));
    }

    public boolean save() {
        return saveToFile(Global.getVarCon("CONF_FILE", String.class));
    }

    public boolean saveDefault() {
        return saveToFile(Global.getVarCon("DEFAULT_CONF_FILE", String.class));
    }

    public boolean loadFromFile(String fileName) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Global.getVarCon("CONF_ROUTE", String.class) + "/" + fileName),
                    "GBK"
            ));
            String s = in.readLine();
            assign(Global.gson.fromJson(s, Config.class));
            in.close();
            Sys.devInfoF("Config Loader", "Data read from '%s'.", fileName);
        } catch(IOException i) {
            Sys.errF("Config Loader", "Unable to open conf file for loading (%s)!", fileName);
            return false;
        }
        return true;
    }

    public boolean saveToFile(String fileName) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Global.getVarCon("CONF_ROUTE", String.class) + "/" + fileName)));
            out.write(Global.gson.toJson(this));
            out.flush();
            out.close();
            Sys.devInfoF("Config Saver", "Json data is saved in '%s'.", fileName);
        } catch(IOException i) {
            Sys.errF("Config Saver", "Unable to open conf file for saving (%s)!", fileName);
            return false;
        }
        return true;
    }
}
