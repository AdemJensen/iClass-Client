package top.chorg.Kernel.Responders.Cmd;

import top.chorg.Kernel.Managers.CmdResponder;
import top.chorg.System.Sys;

public class ExitResponder extends CmdResponder {
    @Override
    public int response() {
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
