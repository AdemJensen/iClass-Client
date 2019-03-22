package top.chorg.System;

import top.chorg.Support.FlagProcessor;
import top.chorg.System.FlagResponders.*;

public class Initializer {
    public static void execute(String[] flagList) {
        // Plugin loading

        //

        registerFlagResponders();

        FlagProcessor.execute(flagList);
    }

    public static void registerFlagResponders() {
        FlagProcessor.register("-dev", new DevModeResponder());
        FlagProcessor.register("-Dev", new DevModeResponder());
        FlagProcessor.register("-DEV", new DevModeResponder());

        FlagProcessor.register("-cmd", new CmdModeResponder());
        FlagProcessor.register("-Cmd", new CmdModeResponder());
        FlagProcessor.register("-CMD", new CmdModeResponder());

        FlagProcessor.register("-gui", new GuiModeResponder());
        FlagProcessor.register("-Gui", new GuiModeResponder());
        FlagProcessor.register("-GUI", new GuiModeResponder());
    }

}
