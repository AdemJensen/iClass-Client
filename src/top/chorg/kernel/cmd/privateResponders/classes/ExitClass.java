package top.chorg.kernel.cmd.privateResponders.classes;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class ExitClass extends CmdResponder {
    public ExitClass(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "exitClass",
                    nextArg()
            ))) {
                Sys.err("Exit Class", "Unable to send request.");
            }
        } else {
            Sys.err("Exit Class", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Exit Class: on invalid result.");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Exit Class", "Successful operation.");
            if (!AuthManager.updateUserInfo()) {
                Sys.info("Alter User", "Problem occurred while refreshing user.");
                AuthManager.bringOffline();
            }
        } else {
            Sys.errF("Exit Class", "Error: %s.", results);
        }
        // TODO: GUI process
        return 0;
    }
}
