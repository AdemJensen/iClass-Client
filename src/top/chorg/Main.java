package top.chorg;

import top.chorg.CmdLine.CmdLineAdapter;
import top.chorg.System.Global;
import top.chorg.System.Initializer;
import top.chorg.System.Sys;

public class Main {

    public static void main(String[] args) {

        Initializer.execute(args);

        if (Sys.isCmdEnv()) {
            CmdLineAdapter.start();
        } else {
            System.out.println("NOT YET USABLE");
        }

    }
}
