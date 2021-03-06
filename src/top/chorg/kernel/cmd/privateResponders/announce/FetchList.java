package top.chorg.kernel.cmd.privateResponders.announce;

import com.google.gson.JsonSyntaxException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.announcements.FetchListResult;
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
            Sys.err("Fetch Announce", "Fatal error while invoking private methods: not enough args.");
            Sys.exit(30);
        }
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchAnnounceList",
                    nextArg()
            ))) {
                Sys.err("Fetch Announce", "Unable to send request.");
                Global.guiAdapter.makeEvent("fetchAnnounceList", "Unable to send request");
            }
        } else {
            Sys.err("Fetch Announce", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("fetchAnnounceList", "User is not online");
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
            Sys.errF("Fetch Announce List", "Error: %s.", arg);
            Global.guiAdapter.makeEvent("fetchAnnounceList", arg);
            Global.dropVar("ANNOUNCE_LIST_INTERNAL");
            return 8;
        }
        if (results == null) {
            HostManager.onInvalidTransmission("Announce fetch: on invalid result.");
            Global.guiAdapter.makeEvent("fetchAnnounceList", "Unknown error");
            Global.dropVar("ANNOUNCE_LIST_INTERNAL");
            return 1;
        }
        if (Global.varExists("ANNOUNCE_LIST_INTERNAL")) {
            Global.setVar("ANNOUNCE_LIST_CACHE", results);
            Global.dropVar("ANNOUNCE_LIST_INTERNAL");
            return 0;
        }
        Global.guiAdapter.makeEvent("fetchAnnounceList", Global.gson.toJson(results));
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%5s|%20s|%70s|%20s|%20s|%10s|%10s|%10s|%10s\n",
                "id", "title", "content", "date", "validity", "class", "level", "publisher", "status"
        );
        for (FetchListResult result : results) {
            Sys.cmdLinePrintF(
                    "%5d|%20s|%70s|%20s|%20s|%10d|%10d|%10d|%10d\n",
                    result.id, result.title, result.content, result.date, result.validity, result.classId,
                    result.level, result.publisher, result.status
            );
        }
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
