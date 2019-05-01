package top.chorg.kernel.cmd.privateResponders.auth;

import com.google.gson.JsonParseException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class GetUsername extends CmdResponder {

    public GetUsername(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "getUserName",
                    nextArg()   // An int array, contains at least one user.
            ))) {
                Sys.err("Get User Name", "Unable to send request.");
            }
        } else {
            Sys.err("Get User Name", "You are not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        String[] info;
        try {
            info = Global.gson.fromJson(results, String[].class);
            if (info == null) throw new JsonParseException("e");
        } catch (JsonParseException e) {
            Sys.errF("Get User Name", "Error: %s", results);
            return 1;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF("Got username: %s\n", results);
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
