package top.chorg.Kernel.Cmd.PublicResponders;

import top.chorg.Kernel.Cmd.CmdResponder;
import top.chorg.System.Sys;

import java.io.Serializable;

public class ExitResponder extends CmdResponder {

    public ExitResponder(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        Sys.info("Cmd Line", "Command line is closing now.");
        Sys.exit(0);
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        return 0;
    }

    @Override
    public String getManual() {
        return "Exit the program with value 0.";
    }
}
