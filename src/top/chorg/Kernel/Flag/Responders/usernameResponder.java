package top.chorg.Kernel.Flag.Responders;

import top.chorg.Kernel.Flag.FlagResponder;
import top.chorg.System.Global;
import top.chorg.System.Sys;

public class usernameResponder extends FlagResponder {
    @Override
    public int response() {
        if (Global.varExists("pre_username")) {
            Sys.warn("Auth", "Username provided more than once, previous inputs are ignored.");
        }
        Global.setVar("pre_username", getArg());
        return 0;
    }

    @Override
    public String getManual() {
        return "Receive username for auth actions. Format: -u [username] or --user [username]. " +
                "Must be used along with '-p' or '--password'.";
    }

    @Override
    public void aftActions() {
        if (!Global.varExists("pre_password")) {
            Sys.warn("Auth", "Password unset, username input is ignored.");
            Global.dropVar("pre_username");
        }
    }
}
