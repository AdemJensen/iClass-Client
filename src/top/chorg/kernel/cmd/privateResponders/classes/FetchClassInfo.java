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

public class FetchClassInfo extends CmdResponder {
    public FetchClassInfo(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchClassInfo",
                    nextArg()
            ))) {
                Sys.err("Fetch Class Info", "Unable to send request.");
            }
        } else {
            Sys.err("Fetch Class Info", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        ClassInfo info;
        try {
            info = Global.gson.fromJson(results, ClassInfo.class);
            if (info == null) throw new JsonParseException("e");
        } catch (JsonParseException e) {
            Sys.errF("Fetch Class Info", "Error: %s", results);
            return 1;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%12s | %d\n" +
                        "%12s | %s\n" +
                        "%12s | %d\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n",
                "id", info.id,
                "name", info.name,
                "avatar", info.avatar,
                "date", info.date.toString(),
                "introduction", info.introduction,
                "classmates", Arrays.toString(info.classmates)
        );
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
