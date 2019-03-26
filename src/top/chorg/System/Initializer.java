package top.chorg.System;

import top.chorg.Kernel.Managers.CmdManager;
import top.chorg.Kernel.Managers.FlagManager;

public class Initializer {
    private static void DEV_PRE_OPERATIONS() {
        Global.clearConfig();
        Global.setConfig("Socket_Host", "127.0.0.1");
        Global.setConfig("Socket_Port", 9999);
        Global.saveConfig();
        Global.reloadConfig();
    }

    public static void execute(String[] flagList) {
        // Plugin loading

        //

        registerGlobalVariables();
        Global.reloadConfig();
        registerFlagResponders();
        registerCommands();

        FlagManager.execute(flagList);

        //DEV_PRE_OPERATIONS();

//        Connector.connect((String) Global.getConfig("Socket_Host"), (int) Global.getConfig("Socket_Port"));
//        Connector.disconnect();
    }

    private static void registerGlobalVariables() {
        Global.setVar("VERSION", "0.0.2");    // Master system version.

        Global.setVar("GUI_MODE", true);    // To determine current display mode.
        Global.setVar("DEV_MODE", false);   // To determine whether this is development mode or not.
        Global.setVar("LOG_ROUTE", "logs/");    // Route of log files.
        Global.setVar("CONF_ROUTE", "");        // Route of route files.
        Global.setVar("DEV_MODE_KEY", "Theresa Apocalypse");    // Development mode key.
        Global.setVar("inProcessReturnValue", -1);  // When a thread is in process, here is its default return value.
    }

    private static void registerFlagResponders() {
        FlagManager.register("-dev", new top.chorg.Kernel.Responders.Flag.DevModeResponder());
        FlagManager.register("-Dev", new top.chorg.Kernel.Responders.Flag.DevModeResponder());
        FlagManager.register("-DEV", new top.chorg.Kernel.Responders.Flag.DevModeResponder());

        FlagManager.register("-cmd", new top.chorg.Kernel.Responders.Flag.CmdModeResponder());
        FlagManager.register("-Cmd", new top.chorg.Kernel.Responders.Flag.CmdModeResponder());
        FlagManager.register("-CMD", new top.chorg.Kernel.Responders.Flag.CmdModeResponder());

        FlagManager.register("-gui", new top.chorg.Kernel.Responders.Flag.GuiModeResponder());
        FlagManager.register("-Gui", new top.chorg.Kernel.Responders.Flag.GuiModeResponder());
        FlagManager.register("-GUI", new top.chorg.Kernel.Responders.Flag.GuiModeResponder());

        FlagManager.register("-u", new top.chorg.Kernel.Responders.Flag.usernameResponder());
        FlagManager.register("--user", new top.chorg.Kernel.Responders.Flag.usernameResponder());
        FlagManager.register("-p", new top.chorg.Kernel.Responders.Flag.passwordResponder());
        FlagManager.register("--password", new top.chorg.Kernel.Responders.Flag.passwordResponder());

        FlagManager.register("-h", new top.chorg.Kernel.Responders.Flag.HelpResponder());
        FlagManager.register("--help", new top.chorg.Kernel.Responders.Flag.HelpResponder());
    }

    private static void registerCommands() {
        CmdManager.register("exit", top.chorg.Kernel.Responders.Cmd.ExitResponder.class);
        CmdManager.register("stop", top.chorg.Kernel.Responders.Cmd.ExitResponder.class);

        CmdManager.register("help", top.chorg.Kernel.Responders.Cmd.HelpResponder.class);
        CmdManager.register("man", top.chorg.Kernel.Responders.Cmd.HelpResponder.class);

        CmdManager.register("login", top.chorg.Kernel.Responders.Cmd.LoginResponder.class);
        CmdManager.register("logon", top.chorg.Kernel.Responders.Cmd.LoginResponder.class);
    }

}
