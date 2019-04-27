package top.chorg.kernel.communication.net.responders.chat;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.communication.api.chat.ChatMsg;
import top.chorg.kernel.communication.net.NetResponder;
import top.chorg.system.Sys;

public class Receive extends NetResponder {
    public Receive(String obj) {
        super(obj);
    }

    @Override
    public int response() {
        ChatMsg msg = getArg(ChatMsg.class);
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "[%d --> %s] %s\n",
                msg.fromId, msg.getSenderStr(), msg.content
        );
        CmdLineAdapter.outputDecoration();
        // TODO: GUI Action.
        return 0;
    }
}
