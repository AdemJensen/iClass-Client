package top.chorg.kernel.cmd.privateResponders.vote;

import com.google.gson.JsonSyntaxException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.vote.FetchListResult;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class FetchList extends CmdResponder {
    public FetchList(String... args) {
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
        if (!hasNextArg()) {
            Sys.err("Fetch Vote List", "Fatal error while invoking private methods: not enough args.");
            Sys.exit(30);
        }
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchVoteList",
                    nextArg()
            ))) {
                Sys.err("Fetch Vote List", "Unable to send request.");
            }
        } else {
            Sys.err("Fetch Vote List", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        FetchListResult[] results;
        String arg = nextArg();
        try {
            results = Global.gson.fromJson(arg, FetchListResult[].class);
        } catch (JsonSyntaxException e) {
            Sys.errF("Fetch Vote List", "Error: %s.", arg);
            return 8;
        }
        if (results == null) {
            HostManager.onInvalidTransmission("Fetch Vote List: on invalid result.");
            return 1;
        }
        if (Global.varExists("VOTE_LIST_INTERNAL")) {
            Global.setVar("VOTE_LIST_CACHE", results);
            Global.dropVar("VOTE_LIST_INTERNAL");
            return 0;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%5s|%20s|%20s|%20s|%10s|%10s|%10s|%10s|%10s|%10s\n",
                "id", "title", "date", "validity", "method", "class", "level", "publisher", "status", "isVoted"
        );
        for (FetchListResult result : results) {
            Sys.cmdLinePrintF(
                    "%5s|%20s|%20s|%20s|%10s|%10s|%10s|%10s|%10s|%10s\n",
                    result.id, result.title, result.date.toString(), result.validity.toString(), result.method,
                    result.classId, result.level, result.publisher, result.status, result.isVoted
            );
        }
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
