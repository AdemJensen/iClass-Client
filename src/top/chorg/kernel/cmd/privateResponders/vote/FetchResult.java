package top.chorg.kernel.cmd.privateResponders.vote;

import com.google.gson.JsonSyntaxException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Arrays;

/**
 * Query the vote results.
 */
public class FetchResult extends CmdResponder {
    public FetchResult(String... args) {
        super(args);
    }

    /**
     * The args are:
     * int, id
     *
     * @return The status
     * @throws IndexOutOfBoundsException When not enough args.
     */
    @Override
    public int response() throws IndexOutOfBoundsException {
        if (!hasNextArg()) {
            Sys.err("Fetch Vote Result", "Fatal error while invoking private methods: not enough args.");
            Sys.exit(30);
        }
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchVoteResult",
                    nextArg()
            ))) {
                Sys.err("Fetch Vote Result", "Unable to send request.");
                Global.guiAdapter.makeEvent("fetchVoteResult", "Unable to send request");
            }
        } else {
            Sys.err("Fetch Vote Result", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("fetchVoteResult", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        int[][] results;
        String arg = nextArg();
        try {
            results = Global.gson.fromJson(arg, int[][].class);
        } catch (JsonSyntaxException e) {
            Sys.errF("Fetch Vote Result", "Error: %s.", arg);
            Global.guiAdapter.makeEvent("fetchVoteResult", arg);
            Global.dropVar("VOTE_RESULT_INTERNAL");
            return 8;
        }
        if (results == null) {
            HostManager.onInvalidTransmission("Fetch Vote Result: on invalid result.");
            Global.guiAdapter.makeEvent("fetchVoteResult", "Unknown error");
            Global.dropVar("VOTE_RESULT_INTERNAL");
            return 1;
        }
        if (Global.varExists("VOTE_RESULT_INTERNAL")) {
            Global.setVar("VOTE_RESULT_CACHE", results);
            Global.dropVar("VOTE_RESULT_INTERNAL");
            return 0;
        }
        Global.guiAdapter.makeEvent("fetchVoteResult", arg);
        Sys.clearLine();
        for (int i = 0; i < results.length; i++) {
            Sys.cmdLinePrintF("Selection %3d | %s\n", i, Arrays.toString(results[i]));
        }
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
