package top.chorg.kernel.cmd.privateResponders.vote;

import com.google.gson.JsonSyntaxException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.vote.FetchInfoResult;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Arrays;

public class FetchInfo extends CmdResponder {

    public FetchInfo(String... args) {
        super(args);
    }

    /**
     * The args are:
     * Stringed integer, Vote id.
     *
     * @return The status
     * @throws IndexOutOfBoundsException When not enough args.
     */
    @Override
    public int response() throws IndexOutOfBoundsException {
        if (!hasNextArg()) {
            Sys.err("Fetch Vote Info", "Fatal error while invoking private methods: not enough args.");
            Sys.exit(30);
        }
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchVoteInfo",
                    nextArg()
            ))) {
                Sys.err("Fetch Vote Info", "Unable to send request.");
            }
        } else {
            Sys.err("Fetch Vote Info", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        FetchInfoResult result;
        String arg = nextArg();
        try {
            result = Global.gson.fromJson(arg, FetchInfoResult.class);
        } catch (JsonSyntaxException e) {
            Sys.errF("Fetch Vote Info", "Error: %s.", arg);
            Global.dropVar("VOTE_INFO_INTERNAL");
            return 8;
        }
        if (result == null) {
            HostManager.onInvalidTransmission("Fetch Vote Info: on invalid result.");
            Global.dropVar("VOTE_INFO_INTERNAL");
            return 1;
        }
        if (Global.varExists("VOTE_INFO_INTERNAL")) {
            Global.setVar("VOTE_INFO_CACHE", result);
            Global.dropVar("VOTE_INFO_INTERNAL");
            return 0;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%12s | %d\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n",
                "id", result.id,
                "title", result.title,
                "content", result.content,
                "selections", result.selections,
                "date", result.date.toString(),
                "validity", result.validity.toString(),
                "method", result.method,
                "class", result.classId,
                "level", result.level,
                "publisher", result.publisher,
                "status(self)", result.status,
                "isVoted", result.isVoted,
                "Operation", Arrays.toString(result.ops)
        );
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }

}
