package top.chorg.kernel.cmd.privateResponders.auth;

import com.google.gson.JsonParseException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class GetNickName extends CmdResponder {

    public GetNickName(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "getNickName",
                    nextArg()   // An int array, contains at least one user.
            ))) {
                Sys.err("Get Nick Name", "Unable to send request.");
                Global.guiAdapter.makeEvent("getNickName", "Unable to send request");
            }
        } else {
            Sys.err("Get Nick Name", "You are not online, please login first.");
            Global.guiAdapter.makeEvent("getNickName", "User is not online");
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
            Sys.errF("Get Nick Name", "Error: %s", results);
            Global.guiAdapter.makeEvent("getNickName", results);
            return 1;
        }
        Global.guiAdapter.makeEvent("getNickName", results);
        Sys.clearLine();
        Sys.cmdLinePrintF("Got NickName: %s\n", results);
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}