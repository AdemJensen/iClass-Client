package top.chorg.kernel.communication.net.responders.announce;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.communication.api.announcements.FetchListResult;
import top.chorg.kernel.communication.net.NetResponder;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class NewAnnounce extends NetResponder {
    public NewAnnounce(String obj) {
        super(obj);
    }

    @Override
    public int response() {
        FetchListResult msg = getArg(FetchListResult.class);
        Global.guiAdapter.makeEvent("onNewAnnounce", Global.gson.toJson(msg));
        Sys.info("New Announce", "New announce received:");
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%5s|%20s|%70s|%20s|%20s|%10s|%10s|%10s|%10s\n",
                "id", "title", "content", "date", "validity", "class", "level", "publisher", "status"
        );
        Sys.cmdLinePrintF(
                "%5d|%20s|%70s|%20s|%20s|%10d|%10d|%10d|%10d\n",
                msg.id, msg.title, msg.content, msg.date, msg.validity, msg.classId,
                msg.level, msg.publisher, msg.status
        );
        CmdLineAdapter.outputDecoration();
        return 0;
    }
}