package top.chorg.kernel.flag.responders;

import top.chorg.kernel.flag.FlagManager;
import top.chorg.kernel.flag.FlagResponder;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class DevMode extends FlagResponder {

    @Override
    public int response() {
        if (Global.varExists("DEV_MODE_MODIFIED")) {
            Sys.warn(
                    "Flags",
                    "The dev mode have been set once! New tag ignored!"
            );
        }
        if (getArg().equals(Global.getVar("DEV_MODE_KEY"))) {
            System.out.println("The system has enabled development mode.");
            Global.setVar("DEV_MODE", true);
            Global.setVar("DEV_MODE_MODIFIED", true);
        } else {
            Sys.warn(
                    "Flags",
                    "The flag '" + FlagManager.getCurFlag() + "' have been ignored!"
            );
        }
        return 0;
    }

    @Override
    public String getManual() {
        return "Include this flag and enter development password to enable the development mode.";
    }

    @Override
    public void aftActions() {
        Global.dropVar("DEV_MODE_MODIFIED");
    }
}
