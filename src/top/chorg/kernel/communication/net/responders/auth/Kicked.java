package top.chorg.kernel.communication.net.responders.auth;

import top.chorg.kernel.communication.net.NetResponder;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class Kicked extends NetResponder {
    public Kicked(String obj) {
        super(obj);
    }

    @Override
    public int response() {
        String msg = getArg();
        Global.guiAdapter.makeEvent("onKicked", msg);
        Sys.warnF("Auth", "You have been kicked from class %s!", msg);
        return 0;
    }
}