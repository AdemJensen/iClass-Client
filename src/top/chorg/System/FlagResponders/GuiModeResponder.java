package top.chorg.System.FlagResponders;

import top.chorg.Support.FlagResponder;
import top.chorg.System.Global;
import top.chorg.System.Sys;

public class GuiModeResponder extends FlagResponder {

    @Override
    public int response() {
        if (Global.GUI_MODE_MODIFIED) {
            Sys.warn(
                    "Flags",
                    "The GUI mode have been set once! Rewriting dev configuration."
            );
        }
        Global.GUI_MODE = true;
        Global.GUI_MODE_MODIFIED = true;
        return 0;
    }

    @Override
    public String getManual() {
        return "Include this flag to enable the GUI mode.";
    }
}
