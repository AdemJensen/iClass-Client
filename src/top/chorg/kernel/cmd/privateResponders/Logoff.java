package top.chorg.kernel.cmd.privateResponders;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;

public class Logoff extends CmdResponder {

    public Logoff(String[] args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        Global.masterSender.send(new Message("logoff", ""));
        AuthManager.bringOffline();
        return 0;
    }
}
