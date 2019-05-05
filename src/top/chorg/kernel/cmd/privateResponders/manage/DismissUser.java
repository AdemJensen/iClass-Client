package top.chorg.kernel.cmd.privateResponders.manage;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class DismissUser extends CmdResponder {
    public DismissUser(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "dismissUser",
                    nextArg()
            ))) {
                Sys.err("Dismiss User", "Unable to send request.");
            }
        } else {
            Sys.err("Dismiss User", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Dismiss User: on invalid result.");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Dismiss User", "Successful operation.");
        } else {
            Sys.errF("Dismiss User", "Error: %s.", results);
        }
        // TODO: GUI process
        return 0;
    }
}