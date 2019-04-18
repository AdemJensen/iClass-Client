package top.chorg.kernel.cmd.publicResponders.Announce;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;

public class FetchTemplate extends CmdResponder {

    public FetchTemplate(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        var resp = Global.cmdManPrivate.execute(
                "fetchAnnounceTemplate",
                "published"
        );
        while (resp.isAlive());
        return 0;
    }

    @Override
    public String getManual() {
        return "Fetch the announcement template list. Use 'templates' to show all the announcements templates " +
                "of your own.";
    }
}
