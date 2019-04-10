package top.chorg.system;

import top.chorg.kernel.flag.FlagManager;

/**
 * Master initializer, register all the global variables and responders.
 * Fully static, no need to make instance.
 */
public class Initializer {
    private static void DEV_PRE_OPERATIONS() {  // Development operations
        Global.conf.Cmd_Server_Host = "127.0.0.1";
        Global.conf.Cmd_Server_Port = 9998;
        Global.conf.File_Server_Host = "127.0.0.1";
        Global.conf.File_Server_Port = 9999;
        Global.conf.save();
        Global.conf.saveDefault();
        Global.conf.load();
    }

    /**
     * Master initialization execution method.
     *
     * @param flagList The arg list passed in at main().
     */
    public static void execute(String[] flagList) {
        // TODO: Plugin loading.

        registerGlobalVariables();

        //DEV_PRE_OPERATIONS();

        if (!Global.conf.load()) {
            Sys.warn("Init", "Unable to load config file, using default config file instead.");
            if (!Global.conf.loadDefault()) {
                Sys.err("Init", "Unable to load default config file.");
                Sys.exit(10);
            }
        }

        registerFlagResponders();
        registerPrivateCommands();
        registerNetResponders();
        registerCommands();

        FlagManager.execute(flagList);  // Start flag option processor.

        //DEV_PRE_OPERATIONS();

    }

    /**
     * Global variable register.
     * Register all the initial global variables.
     */
    private static void registerGlobalVariables() {
        Global.setVar("VERSION", "0.0.3");    // Master system version.

        Global.setVar("DEV_MODE_KEY", "Theresa Apocalypse");    // Development mode key.
        Global.setVar("PROCESS_RETURN", -1);  // When a thread is in process, here is its default return value.

        Global.setVar("GUI_MODE", true);        // To determine current display mode.
        Global.setVar("DEV_MODE", false);       // To determine whether this is development mode or not.

        Global.setVar("LOG_ROUTE", "./logs");   // Route of log files.
        Global.setVar("CONF_ROUTE", ".");       // Route of route files.
        Global.setVar("CONF_FILE", "config.json");                      // Config file name.
        Global.setVar("DEFAULT_CONF_FILE", "config.default.json");      // Default config file name.

    }

    /**
     * Flag Responder register.
     * Register all the flag responders.
     */
    private static void registerFlagResponders() {
        FlagManager.register("-dev", new top.chorg.kernel.flag.responders.DevMode());
        FlagManager.register("-Dev", new top.chorg.kernel.flag.responders.DevMode());
        FlagManager.register("-DEV", new top.chorg.kernel.flag.responders.DevMode());

        FlagManager.register("-cmd", new top.chorg.kernel.flag.responders.CmdMode());
        FlagManager.register("-Cmd", new top.chorg.kernel.flag.responders.CmdMode());
        FlagManager.register("-CMD", new top.chorg.kernel.flag.responders.CmdMode());

        FlagManager.register("-gui", new top.chorg.kernel.flag.responders.GuiMode());
        FlagManager.register("-Gui", new top.chorg.kernel.flag.responders.GuiMode());
        FlagManager.register("-GUI", new top.chorg.kernel.flag.responders.GuiMode());

        FlagManager.register("-h", new top.chorg.kernel.flag.responders.Help());
        FlagManager.register("--help", new top.chorg.kernel.flag.responders.Help());

        FlagManager.register("-u", new top.chorg.kernel.flag.responders.username());
        FlagManager.register("--user", new top.chorg.kernel.flag.responders.username());
        FlagManager.register("-p", new top.chorg.kernel.flag.responders.password());
        FlagManager.register("--password", new top.chorg.kernel.flag.responders.password());
    }

    /**
     * Private command Responder register.
     * Register all the command responders.
     */
    private static void registerPrivateCommands() {

        Global.cmdManPrivate.register("login", top.chorg.kernel.cmd.privateResponders.Login.class);
        Global.cmdManPrivate.register("register", top.chorg.kernel.cmd.privateResponders.Register.class);
        Global.cmdManPrivate.register("logoff", top.chorg.kernel.cmd.privateResponders.Logoff.class);

    }

    private static void registerNetResponders() {

    }

    /**
     * Public command Responder register.
     * Register all the command responders.
     */
    private static void registerCommands() {

        Global.cmdManPublic.register("exit", top.chorg.kernel.cmd.publicResponders.Exit.class);
        Global.cmdManPublic.register("stop", top.chorg.kernel.cmd.publicResponders.Exit.class);

        Global.cmdManPublic.register("help", top.chorg.kernel.cmd.publicResponders.Help.class);
        Global.cmdManPublic.register("man", top.chorg.kernel.cmd.publicResponders.Help.class);

        Global.cmdManPublic.register("login", top.chorg.kernel.cmd.publicResponders.NormalLogin.class);
        Global.cmdManPublic.register("logon", top.chorg.kernel.cmd.publicResponders.NormalLogin.class);

        Global.cmdManPublic.register("register", top.chorg.kernel.cmd.publicResponders.Register.class);
        Global.cmdManPublic.register("reg", top.chorg.kernel.cmd.publicResponders.Register.class);

        Global.cmdManPublic.register("logoff", top.chorg.kernel.cmd.publicResponders.Logoff.class);
        Global.cmdManPublic.register("offline", top.chorg.kernel.cmd.publicResponders.Logoff.class);

    }

}
