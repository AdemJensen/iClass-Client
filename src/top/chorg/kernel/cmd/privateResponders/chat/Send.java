package top.chorg.kernel.cmd.privateResponders.chat;

import com.google.gson.JsonParseException;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.chat.ChatMsg;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class Send extends CmdResponder {

    public Send(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "sendChat",
                    Global.gson.toJson(new ChatMsg(
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class)),
                            nextArg()
                    ))
            ))) {
                Sys.errF("Send Chat", "Unable to send chat message.");
                return 2;
            }
        } else {
            Sys.err("Send Chat", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        ChatMsg msg;
        String str = nextArg();
        try {
            msg = Global.gson.fromJson(str, ChatMsg.class);
        } catch (JsonParseException e) {
            Sys.errF("Send Chat", "Error: %s", str);
            return 1;
        }
        Sys.infoF("Send Chat", "Successful: --> [%s %d) ", msg.type == 1 ? "class" : "user", msg.toId);
        // TODO: GUI Action.
        return 0;
    }
}
