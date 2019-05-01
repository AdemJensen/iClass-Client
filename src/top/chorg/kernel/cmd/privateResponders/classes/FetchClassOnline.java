package top.chorg.kernel.cmd.privateResponders.classes;

import com.google.gson.JsonParseException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Arrays;

public class FetchClassOnline extends CmdResponder {
    public FetchClassOnline(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchOnline",
                    nextArg()
            ))) {
                Sys.err("Fetch Class Online", "Unable to send request.");
            }
        } else {
            Sys.err("Fetch Class Online", "User is not online, please login first.");
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
            Sys.errF("Fetch Class Online", "Error: %s", results);
            return 1;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF("Online user: %s\n", Arrays.toString(info));
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
