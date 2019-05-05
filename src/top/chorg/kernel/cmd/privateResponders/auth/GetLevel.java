package top.chorg.kernel.cmd.privateResponders.auth;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class GetLevel extends CmdResponder {
    public GetLevel(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "getLevel",
                    nextArg()   // An int array, contains at least one user.
            ))) {
                Sys.err("Get Level", "Unable to send request.");
                Global.guiAdapter.makeEvent("getLevel", "Unable to send request");
            }
        } else {
            Sys.err("Get Level", "You are not online, please login first.");
            Global.guiAdapter.makeEvent("getLevel", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        try {
            Integer.parseInt(results);
        } catch (NumberFormatException e) {
            Sys.errF("Get Level", "Error: %s", results);
            Global.guiAdapter.makeEvent("getLevel", results);
            return 1;
        }
        Global.guiAdapter.makeEvent("getLevel", results);
        Sys.clearLine();
        Sys.cmdLinePrintF("Got user level: %s\n", results);
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
