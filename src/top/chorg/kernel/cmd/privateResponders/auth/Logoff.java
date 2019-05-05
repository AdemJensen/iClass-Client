package top.chorg.kernel.cmd.privateResponders.auth;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;

public class Logoff extends CmdResponder {

    public Logoff(String...args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        AuthManager.bringOffline();
        return 0;
    }
}
