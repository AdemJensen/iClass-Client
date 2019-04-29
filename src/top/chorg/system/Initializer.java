package top.chorg.system;

import top.chorg.kernel.communication.net.NetManager;
import top.chorg.kernel.flag.FlagManager;
import top.chorg.support.JarLoader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

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
        Global.conf.modList = new String[0];
        Global.conf.saveDefault();
        Global.conf.modList = new String[]{"iClass GUI.jar"};
        Global.conf.save();
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

        loadMods();

    }

    private static void loadMods() {
        for (String mod : Global.conf.modList) {
            try {
                JarLoader a = new JarLoader(new URL[]{new URL("file://" + new File("extensions/iClass GUI.jar").getAbsolutePath())});
                Class<?> cl = a.loadClass("top.chorg.ForeGuiAdapter");
                //System.out.println(cl.toString());
                Method testMethod = cl.getMethod("init");
                testMethod.invoke(
                        cl.getDeclaredConstructor().newInstance()
                );
                Sys.devInfoF("Mod loader", "Loaded mod: %s", mod);
            } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException
                    | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                Sys.errF("Mod loader", "Error while loading mod '%s': %s", mod, e.getMessage());
            }
        }
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

        Global.cmdManPrivate.register("login", top.chorg.kernel.cmd.privateResponders.auth.Login.class);
        Global.cmdManPrivate.register("register", top.chorg.kernel.cmd.privateResponders.auth.Register.class);
        Global.cmdManPrivate.register("logoff", top.chorg.kernel.cmd.privateResponders.auth.Logoff.class);

        Global.cmdManPrivate.register(
                "fetchAnnounceList",
                top.chorg.kernel.cmd.privateResponders.announce.FetchList.class
        );
        Global.cmdManPrivate.register(
                "fetchAnnounceTemplate",
                top.chorg.kernel.cmd.privateResponders.announce.FetchTemplate.class
        );
        Global.cmdManPrivate.register(
                "addAnnounce",
                top.chorg.kernel.cmd.privateResponders.announce.Add.class
        );
        Global.cmdManPrivate.register(
                "addAnnounceTemplate",
                top.chorg.kernel.cmd.privateResponders.announce.AddTemplate.class
        );
        Global.cmdManPrivate.register(
                "alterAnnounce",
                top.chorg.kernel.cmd.privateResponders.announce.Alter.class
        );
        Global.cmdManPrivate.register(
                "alterAnnounceTemplate",
                top.chorg.kernel.cmd.privateResponders.announce.AlterTemplate.class
        );
        Global.cmdManPrivate.register(
                "delAnnounce",
                top.chorg.kernel.cmd.privateResponders.announce.Del.class
        );
        Global.cmdManPrivate.register(
                "delAnnounceTemplate",
                top.chorg.kernel.cmd.privateResponders.announce.DelTemplate.class
        );

        Global.cmdManPrivate.register(
                "fetchVoteList",
                top.chorg.kernel.cmd.privateResponders.vote.FetchList.class
        );
        Global.cmdManPrivate.register(
                "fetchVoteInfo",
                top.chorg.kernel.cmd.privateResponders.vote.FetchInfo.class
        );
        Global.cmdManPrivate.register(
                "addVote",
                top.chorg.kernel.cmd.privateResponders.vote.Add.class
        );
        Global.cmdManPrivate.register(
                "alterVote",
                top.chorg.kernel.cmd.privateResponders.vote.Alter.class
        );
        Global.cmdManPrivate.register(
                "delVote",
                top.chorg.kernel.cmd.privateResponders.vote.Del.class
        );
        Global.cmdManPrivate.register(
                "makeVote",
                top.chorg.kernel.cmd.privateResponders.vote.Make.class
        );
        Global.cmdManPrivate.register(
                "fetchVoteResult",
                top.chorg.kernel.cmd.privateResponders.vote.FetchResult.class
        );

        Global.cmdManPrivate.register("sendChat", top.chorg.kernel.cmd.privateResponders.chat.Send.class);
        Global.cmdManPrivate.register(
                "fetchChatHistory",
                top.chorg.kernel.cmd.privateResponders.chat.FetchHistory.class
        );

        Global.cmdManPrivate.register("uploadFile", top.chorg.kernel.cmd.privateResponders.file.Upload.class);
        Global.cmdManPrivate.register("downloadFile", top.chorg.kernel.cmd.privateResponders.file.Download.class);
        Global.cmdManPrivate.register(
                "fetchFileList",
                top.chorg.kernel.cmd.privateResponders.file.FetchList.class
        );
        Global.cmdManPrivate.register(
                "fetchFileInfo",
                top.chorg.kernel.cmd.privateResponders.file.FetchInfo.class
        );

    }

    private static void registerNetResponders() {
        NetManager.register("onChat", top.chorg.kernel.communication.net.responders.chat.Receive.class);
    }

    /**
     * Public command Responder register.
     * Register all the command responders.
     */
    private static void registerCommands() {

        Global.cmdManPublic.register("exit", top.chorg.kernel.cmd.publicResponders.sys.Exit.class);
        Global.cmdManPublic.register("stop", top.chorg.kernel.cmd.publicResponders.sys.Exit.class);

        Global.cmdManPublic.register("help", top.chorg.kernel.cmd.publicResponders.sys.Help.class);
        Global.cmdManPublic.register("man", top.chorg.kernel.cmd.publicResponders.sys.Help.class);

        Global.cmdManPublic.register("login", top.chorg.kernel.cmd.publicResponders.auth.NormalLogin.class);
        Global.cmdManPublic.register("logon", top.chorg.kernel.cmd.publicResponders.auth.NormalLogin.class);

        Global.cmdManPublic.register("register", top.chorg.kernel.cmd.publicResponders.auth.Register.class);
        Global.cmdManPublic.register("reg", top.chorg.kernel.cmd.publicResponders.auth.Register.class);

        Global.cmdManPublic.register("logoff", top.chorg.kernel.cmd.publicResponders.auth.Logoff.class);
        Global.cmdManPublic.register("logout", top.chorg.kernel.cmd.publicResponders.auth.Logoff.class);

        Global.cmdManPublic.register("self", top.chorg.kernel.cmd.publicResponders.auth.Self.class);
        Global.cmdManPublic.register("me", top.chorg.kernel.cmd.publicResponders.auth.Self.class);
        Global.cmdManPublic.register("i", top.chorg.kernel.cmd.publicResponders.auth.Self.class);
        Global.cmdManPublic.register("I", top.chorg.kernel.cmd.publicResponders.auth.Self.class);

        Global.cmdManPublic.register(
                "announce",
                top.chorg.kernel.cmd.publicResponders.announce.Announce.class
        );
        Global.cmdManPublic.register(
                "template",
                top.chorg.kernel.cmd.publicResponders.announce.Template.class
        );

        Global.cmdManPublic.register(
                "vote",
                top.chorg.kernel.cmd.publicResponders.vote.Vote.class
        );

        Global.cmdManPublic.register("chat", top.chorg.kernel.cmd.publicResponders.chat.Chat.class);
        Global.cmdManPublic.register("file", top.chorg.kernel.cmd.publicResponders.file.File.class);
    }

}
