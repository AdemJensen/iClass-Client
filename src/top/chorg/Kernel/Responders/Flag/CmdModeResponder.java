package top.chorg.Kernel.Responders.Flag;

import top.chorg.Kernel.Managers.FlagResponder;
import top.chorg.System.Global;
import top.chorg.System.Sys;

public class CmdModeResponder extends FlagResponder {

    @Override
    public int response() {
        if (Global.varExists("GUI_MODE_MODIFIED")) {
            Sys.warn(
                    "Flags",
                    "The display mode have been set once! Rewriting old configuration."
            );
        }
        Global.setVar("GUI_MODE", false);
        Global.setVar("GUI_MODE_MODIFIED", true);
        return 0;
    }

    @Override
    public String getManual() {
        return "Include this flag to enable the cmd mode.";
    }

    @Override
    public void aftActions() {
        Global.dropVar("GUI_MODE_MODIFIED");
    }
}
