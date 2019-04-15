package top.chorg.kernel.cmd.publicResponders;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;

public class FetchAnnounceList extends CmdResponder {

    public FetchAnnounceList(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (hasNextArg()) {
                if (nextArg().equals("published")) {
                    var resp = Global.cmdManPrivate.execute(
                            "fetchAnnounceList",
                            "0",
                            "0",
                            String.valueOf(AuthManager.getUser().getId())
                    );
                    while (resp.isAlive());
                }
            } else {
                int level;
                switch (AuthManager.getUser().getUserGroup()) {
                    case 'A':
                        level = 1;
                        break;
                    case 'S':
                        level = 2;
                        break;
                    default:
                        level = 0;
                }
                var resp = Global.cmdManPrivate.execute(
                        "fetchAnnounceList",
                        String.valueOf(AuthManager.getUser().getClassId()),
                        String.valueOf(level),
                        "0"
                );
                while (resp.isAlive());
            }
        }
        return 0;
    }

    @Override
    public String getManual() {
        return "Fetch the announcement list. Use 'announcements published' to show all the announcements published " +
                "by your self.";
    }
}
