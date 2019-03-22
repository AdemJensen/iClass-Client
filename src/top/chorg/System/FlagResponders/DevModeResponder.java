package top.chorg.System.FlagResponders;

import top.chorg.Support.FlagResponder;
import top.chorg.System.Global;
import top.chorg.System.Sys;

public class DevModeResponder extends FlagResponder {

    @Override
    public int execute() {
        if (Global.DEV_MODE_MODIFIED) {
            Sys.warn(
                    "Flags",
                    "The dev mode have been set once! Rewriting dev configuration."
            );
        }
        Global.DEV_MODE = true;
        Global.DEV_MODE_MODIFIED = true;
        return 0;
    }

    @Override
    public String getManual() {
        return "Include this flag to enable the development mode.";
    }
}
