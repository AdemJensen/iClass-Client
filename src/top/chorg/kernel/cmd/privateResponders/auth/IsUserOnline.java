package top.chorg.kernel.cmd.privateResponders.auth;

import com.google.gson.JsonParseException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class IsUserOnline extends CmdResponder {
    public IsUserOnline(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "judgeOnline",
                    nextArg()   // An int array, contains at least one user.
            ))) {
                Sys.err("Judge Online", "Unable to send request.");
            }
        } else {
            Sys.err("Judge Online", "You are not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        int[] info;
        try {
            info = Global.gson.fromJson(results, int[].class);
            if (info == null) throw new JsonParseException("e");
        } catch (JsonParseException e) {
            Sys.errF("Judge Online", "Error: %s", results);
            return 1;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF("User online status: %s\n", results);
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
