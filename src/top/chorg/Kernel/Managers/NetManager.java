package top.chorg.Kernel.Managers;

import top.chorg.Kernel.Communication.Message;
import top.chorg.System.Sys;

import java.util.HashMap;

public class NetManager {
    // TODO: Not formed.
    private static HashMap<String, NetResponder> records = new HashMap<>();

    public static int execute(Message msg) {
        if (!records.containsKey(msg.msgType)) {
            Sys.warnF(
                    "Net",
                    "Incoming invalid transmission (code %d), ignored.",
                    msg.msgType
            );
            return 0;
        }
        int returnValue = records.get(msg.msgType).response();
        if (returnValue != 0) {
            Sys.warnF(
                    "Net",
                    "Net responder (%d) returned error code (%d).",
                    returnValue
            );
            Sys.exit(150 + returnValue);
        }
        return returnValue;
    }

    public static void register(String cmd, NetResponder response) {
        if (records.containsKey(cmd)) {
            Sys.errF(
                    "CMD",
                    "CMD '%s' already exists!",
                    cmd
            );
            Sys.exit(9);
        }
        records.put(cmd, response);
    }
}
