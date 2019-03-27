package top.chorg;

import top.chorg.CmdLine.CmdLineAdapter;
import top.chorg.System.Initializer;
import top.chorg.System.Sys;

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

        if (Sys.isCmdEnv()) {
            CmdLineAdapter.start();     // Start Command Line mode.
        } else {
            // TODO: Start GUI mode.
            System.out.println("NOT YET USABLE");
        }

    }
}
