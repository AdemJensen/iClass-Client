package top.chorg;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.gui.EmptyGuiAdapter;
import top.chorg.system.Global;
import top.chorg.system.Initializer;
import top.chorg.system.Sys;

/**
 * Master execution class, all things start from here.
 */
public class Main {

    /**
     * Master execution method. Program starts from here.
     *
     * @param args Flags passed from command line.
     */
    public static void main(String[] args) {

        Initializer.execute(args);      // Start initialization.

        boolean cmdLineIsStarted = false;
        if (Sys.isCmdEnv()) {
            Global.guiAdapter = new EmptyGuiAdapter();
            CmdLineAdapter.start();     // Start Command Line mode.
            cmdLineIsStarted = true;
        } else {
            if (Global.guiAdapter == null) {
                Global.setVar("GUI_MODE", false);
                CmdLineAdapter.start();
                cmdLineIsStarted = true;
                Sys.warn("Init", "GUI module not found, Cmd mode enabled.");
                Global.guiAdapter = new EmptyGuiAdapter();
            } else {
                Global.guiAdapter.makeEvent("startup");
            }
        }
        if (Sys.isDevEnv() && !cmdLineIsStarted) {
            CmdLineAdapter.start();
        }

    }
}
