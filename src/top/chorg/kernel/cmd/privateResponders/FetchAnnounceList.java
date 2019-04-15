package top.chorg.kernel.cmd.privateResponders;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.announcements.FetchListRequest;
import top.chorg.kernel.communication.api.announcements.FetchListResult;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class FetchAnnounceList extends CmdResponder {

    public FetchAnnounceList(String... args) {
        super(args);
    }

    /**
     * The args are:
     * - class id
     * - level
     * - publisher id
     *
     * @return The status
     * @throws IndexOutOfBoundsException When not enough args.
     */
    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchAnnounceList",
                    Global.gson.toJson(new FetchListRequest(
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class))
                    ))
            ))) {
                Sys.err("Announce Fetch", "Unable to send request.");
            }
        } else {
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        FetchListResult[] results = nextArg(FetchListResult[].class);
        if (results == null) {
            HostManager.onInvalidTransmission("Announce fetch: on invalid result.");
            return 1;
        }
        Sys.info("Announce Fetch", "Got following info:");
        Sys.cmdLinePrintF(
                "%5s|%20s|%70s|%20s|%20s|%10s|%10s|%10s\n",
                "id", "title", "content", "date", "validity", "class", "level", "publisher"
        );
        for (FetchListResult result : results) {
            Sys.cmdLinePrintF(
                    "%5d|%70s|%50s|%20s|%20s|%10d|%10d|%10d\n",
                    result.id, result.title, result.content, result.date, result.validity, result.classId,
                    result.level, result.publisher
            );
        }
        // TODO: GUI process
        return 0;
    }
}
