package top.chorg.kernel.cmd.privateResponders.announce;

import com.google.gson.JsonSyntaxException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.announcements.FetchTemplateResult;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class FetchTemplate extends CmdResponder {

    public FetchTemplate(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchAnnounceTemplate",
                    ""
            ))) {
                Sys.err("Fetch Template", "Unable to send request.");
            }
        } else {
            Sys.err("Fetch Template", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        FetchTemplateResult[] results;
        String arg = nextArg();
        try {
            results = Global.gson.fromJson(arg, FetchTemplateResult[].class);
        } catch (JsonSyntaxException e) {
            Sys.errF("Fetch Announce List", "Error: %s.", arg);
            return 8;
        }
        if (results == null) {
            HostManager.onInvalidTransmission("Template fetch: on invalid result.");
            return 1;
        }
        if (Global.varExists("TEMPLATE_LIST_INTERNAL")) {
            Global.setVar("TEMPLATE_LIST_CACHE", results);
            Global.dropVar("TEMPLATE_LIST_INTERNAL");
            return 0;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%5s|%20s|%20s|%70s\n",
                "id", "name", "title", "content"
        );
        for (FetchTemplateResult result : results) {
            Sys.cmdLinePrintF(
                    "%5d|%20s|%20s|%70s\n",
                    result.id, result.name, result.title, result.content
            );
        }
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
