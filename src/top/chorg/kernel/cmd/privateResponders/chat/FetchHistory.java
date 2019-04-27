package top.chorg.kernel.cmd.privateResponders.chat;

import com.google.gson.JsonSyntaxException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.announcements.FetchListResult;
import top.chorg.kernel.communication.api.chat.FetchHistoryRequest;
import top.chorg.kernel.communication.api.chat.History;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class FetchHistory extends CmdResponder {

    public FetchHistory(String... args) {
        super(args);
    }

    /**
     * The args are:
     * String, "published" / "all"
     *
     * @return The status
     * @throws IndexOutOfBoundsException When not enough args.
     */
    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchChatHistory",
                    Global.gson.toJson(new FetchHistoryRequest(
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class))
                    ))
            ))) {
                Sys.err("Fetch Chat History", "Unable to send request.");
            }
        } else {
            Sys.err("Fetch Chat History", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        History[] results;
        String arg = nextArg();
        try {
            results = Global.gson.fromJson(arg, History[].class);
        } catch (JsonSyntaxException e) {
            Sys.errF("Fetch Chat History", "Error: %s.", arg);
            Global.dropVar("CHAT_HISTORY_LIST_INTERNAL");
            return 8;
        }
        if (results == null) {
            HostManager.onInvalidTransmission("Fetch Chat History: on invalid result.");
            Global.dropVar("CHAT_HISTORY_LIST_INTERNAL");
            return 1;
        }
        if (Global.varExists("CHAT_HISTORY_LIST_INTERNAL")) {
            Global.setVar("CHAT_HISTORY_LIST_CACHE", results);
            Global.dropVar("CHAT_HISTORY_LIST_INTERNAL");
            return 0;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%5s|%20s|%6s| %s\n",
                "id", "time", "from", "content"
        );
        for (History result : results) {
            Sys.cmdLinePrintF(
                    "%5s|%20s|%6d| %s\n",
                    result.id, result.time.toString(), result.fromId, result.content
            );
        }
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
