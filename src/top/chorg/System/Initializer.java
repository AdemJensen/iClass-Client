package top.chorg.System;

import top.chorg.Kernel.Communication.Connector;
import top.chorg.Support.FlagProcessor;
import top.chorg.System.FlagResponders.*;

import static top.chorg.System.Global.glob;

public class Initializer {
    private static void DEV_PRE_OPERATIONS() {
        Sys.clearConfig();
        glob.setConfig("Socket_Host", "127.0.0.1");
        glob.setConfig("Socket_Port", 9999);
        Sys.saveConfig();
        Sys.reloadConfig();
    }

    public static void execute(String[] flagList) {
        // Plugin loading

        //

        registerFlagResponders();

        FlagProcessor.execute(flagList);

        //DEV_PRE_OPERATIONS();

        Connector.connect((String) glob.getConfig("Socket_Host"), (int) glob.getConfig("Socket_Port"));
        Connector.disconnect();
    }

    public static void registerFlagResponders() {
        FlagProcessor.register("-dev", new DevModeResponder());
        FlagProcessor.register("-Dev", new DevModeResponder());
        FlagProcessor.register("-DEV", new DevModeResponder());

        FlagProcessor.register("-cmd", new CmdModeResponder());
        FlagProcessor.register("-Cmd", new CmdModeResponder());
        FlagProcessor.register("-CMD", new CmdModeResponder());

        FlagProcessor.register("-gui", new GuiModeResponder());
        FlagProcessor.register("-Gui", new GuiModeResponder());
        FlagProcessor.register("-GUI", new GuiModeResponder());
    }

}
