package top.chorg.kernel.cmd.publicResponders.announce;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;

public class FetchList extends CmdResponder {

    public FetchList(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (hasNextArg()) {
                if (nextArg().equals("published")) {
                    var resp = Global.cmdManPrivate.execute(
                            "fetchAnnounceList",
                            "published"
                    );
                    while (resp.isAlive());
                }
            } else {
                var resp = Global.cmdManPrivate.execute(
                        "fetchAnnounceList",
                        "all"
                );
                while (resp.isAlive());
            }
        }
        return 0;
    }

    @Override
    public String getManual() {
        return "Fetch the announcement list. Use 'announce published' to show all the announcements published " +
                "by your self.";
    }
}
