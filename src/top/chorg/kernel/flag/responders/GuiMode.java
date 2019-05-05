package top.chorg.kernel.flag.responders;

import top.chorg.kernel.flag.FlagResponder;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class GuiMode extends FlagResponder {

    @Override
    public int response() {
        if (Global.varExists("GUI_MODE_MODIFIED")) {
            Sys.warn(
                    "Flags",
                    "The display mode have been set once! Rewriting old configuration."
            );
        }
        Global.setVar("GUI_MODE", true);
        Global.setVar("GUI_MODE_MODIFIED", true);
        return 0;
    }

    @Override
    public String getManual() {
        return "Include this flag to enable the GUI mode.";
    }

    @Override
    public void aftActions() {
        Global.dropVar("GUI_MODE_MODIFIED");
    }
}
