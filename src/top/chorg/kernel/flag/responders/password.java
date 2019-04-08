package top.chorg.kernel.flag.responders;

import top.chorg.kernel.flag.FlagResponder;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class password extends FlagResponder {
    @Override
    public int response() {
        if (Global.varExists("pre_password")) {
            Sys.warn("Auth", "Password provided more than once, previous inputs are ignored.");
        }
        Global.setVar("pre_password", getArg());
        return 0;
    }

    @Override
    public String getManual() {
        return "Receive password for auth actions. Format: -p [password] or --password [password]. " +
                "Must be used along with '-u' or '--user'.";
    }

    @Override
    public void aftActions() {
        if (Global.varExists("pre_username")) {
            Global.setVar("pre_auth_ready", true);
        } else {
            Sys.warn("Auth", "Username unset, password input is ignored.");
            Global.dropVar("pre_password");
        }
    }
}
