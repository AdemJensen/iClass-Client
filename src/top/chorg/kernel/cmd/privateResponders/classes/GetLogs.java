package top.chorg.kernel.cmd.privateResponders.classes;

import com.google.gson.JsonParseException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.auth.ClassInfo;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Arrays;

public class GetLogs extends CmdResponder {
    public GetLogs(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "getLog",
                    nextArg()
            ))) {
                Sys.err("Fetch Log", "Unable to send request.");
                Global .guiAdapter.makeEvent("getLog", "Unable to send request");
            }
        } else {
            Sys.err("Fetch Log", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("getLog", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        Sys.clearLine();
        Sys.cmdLinePrint(results);
        CmdLineAdapter.outputDecoration();
        Global.guiAdapter.makeEvent("getLog", results);
        // TODO: GUI process
        return 0;
    }
}
