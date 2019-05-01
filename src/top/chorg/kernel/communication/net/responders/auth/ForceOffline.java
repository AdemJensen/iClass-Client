package top.chorg.kernel.communication.net.responders.auth;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.kernel.communication.net.NetResponder;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class ForceOffline extends NetResponder {
    public ForceOffline(String obj) {
        super(obj);
    }

    @Override
    public int response() {
        String msg = getArg();
        AuthManager.bringOffline();
        Sys.warnF("Force", "You have been forced offline: %s.", msg);
        CmdLineAdapter.outputDecoration();
        Global.guiAdapter.makeEvent("forceOffline", msg);
        return 0;
    }
}
