package top.chorg.kernel.cmd.publicResponders.Auth;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class Logoff extends CmdResponder {

    public Logoff(String...args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (!AuthManager.isOnline()) {
            Sys.err("Auth", "User not online, cannot log off!");
            return 1;
        }
        CmdResponder resp =  Global.cmdManPrivate.execute("logoff");
        while (resp.isAlive()) { }
        Sys.info("Auth", "User successfully logged off.");
        return 0;
    }
}
