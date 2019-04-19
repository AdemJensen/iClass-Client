package top.chorg.kernel.cmd.privateResponders.Announce;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class Del extends CmdResponder {

    public Del(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "delAnnounce",
                    nextArg()
            ))) {
                Sys.err("Delete Announce", "Unable to send request.");
            }
        } else {
            Sys.err("Delete Announce", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Delete Announce: on invalid result.");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Delete Announce", "Successful operation.");
        } else {
            Sys.errF("Delete Announce", "Error: %s.", results);
        }
        // TODO: GUI process
        return 0;
    }
}
