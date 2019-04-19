package top.chorg.kernel.cmd.privateResponders.Announce;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class DelTemplate extends CmdResponder {

    public DelTemplate(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "delAnnounceTemplate",
                    nextArg()
            ))) {
                Sys.err("Delete Template", "Unable to send request.");
            }
        } else {
            Sys.err("Delete Template", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Delete Template: on invalid result.");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Delete Template", "Successful operation.");
        } else {
            Sys.errF("Delete Template", "Error: %s.", results);
        }
        // TODO: GUI process
        return 0;
    }
}

