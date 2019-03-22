package top.chorg.System.FlagResponders;

import top.chorg.Support.FlagProcessor;
import top.chorg.Support.FlagResponder;
import top.chorg.System.Global;
import top.chorg.System.Sys;

public class DevModeResponder extends FlagResponder {

    @Override
    public int response() {
        if (Global.DEV_MODE_MODIFIED) {
            Sys.warn(
                    "Flags",
                    "The dev mode have been set once! Rewriting dev configuration."
            );
        }
        if (getArg().equals("123456")) {
            Global.DEV_MODE = true;
            Global.DEV_MODE_MODIFIED = true;
        } else {
            Sys.warn(
                    "Flags",
                    "The flag '" + FlagProcessor.getCurFlag() + "' have been ignored!"
            );
        }
        return 0;
    }

    @Override
    public String getManual() {
        return "Include this flag and enter development password to enable the development mode.";
    }
}
