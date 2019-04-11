package top.chorg.kernel.cmd.publicResponders;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.system.Sys;

public class Exit extends CmdResponder {

    public Exit(String...args) {
        super(args);
    }

    @Override
    public int response() {
        Sys.info("Cmd Line", "Command line is closing now.");
        Sys.exit(0);
        return 0;
    }

    @Override
    public String getManual() {
        return "Exit the program with value 0.";
    }
}
