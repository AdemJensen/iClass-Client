package top.chorg.kernel.cmd.publicResponders.chat;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class Chat extends CmdResponder {

    public Chat(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            String cmd = nextArg();
            CmdResponder resp;
            switch (cmd) {
                case "send":
                    resp = sendChat();
                    break;
                case "history":
                    resp = getHistory();
                    break;
                default:
                    Sys.errF("Chat", "Cmd '%s' is invalid.", cmd);
                    return 1;
            }
            if (resp == null) {
                Sys.err("Chat", "Something went wrong when executing commands.");
                return 2;
            } else {
                while (resp.isAlive()) {}
            }
        } else {
            Sys.err("Chat", "User is not online, please login first.");
        }
        return 0;
    }

    private CmdResponder sendChat() {
        try {
            int type = Objects.requireNonNull(nextArg(int.class));
            int toId = Objects.requireNonNull(nextArg(int.class));
            String content = nextArg();
            return Global.cmdManPrivate.execute(
                    "sendChat",
                    String.valueOf(type), String.valueOf(toId), content
            );
        } catch (NullPointerException e) {
            Sys.err("Send Chat", "Too few arguments, type 'help' for more info.");
            return null;
        }
    }

    private CmdResponder getHistory() {
        try {
            int type = Objects.requireNonNull(nextArg(int.class));
            int toId = Objects.requireNonNull(nextArg(int.class));
            return Global.cmdManPrivate.execute(
                    "fetchChatHistory",
                    String.valueOf(type), String.valueOf(toId)
            );
        } catch (NullPointerException e) {
            Sys.err("Get History", "Too few arguments, type 'help' for more info.");
            return null;
        }
    }

    @Override
    public String getManual() {
        return "To make actions that relevant to chatting system. \n " +
                "\t\t- send [type] [targetId] [content]\t\tSend message to specific target.\n\t\t\t" +
                "If type = 1, then targetId is the classId. If type = 2, then targetId is the userId.\n" +
                "\t\t- history [type] [targetId]\t\tQuery chatting history.\n\t\t\t" +
                "If type = 1, then targetId is the classId. If type = 2, then targetId is the userId.\n" +
                "\t\t- alter\t\tAlter an template. There will be a guidance system guiding you create a template.\n" +
                "\t\t- del [templateId]\t\tDelete an template. Warning: this action is irreversible.";
    }
}
