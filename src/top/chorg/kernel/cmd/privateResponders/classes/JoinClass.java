package top.chorg.kernel.cmd.privateResponders.classes;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class JoinClass extends CmdResponder {
    public JoinClass(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "joinClass",
                    nextArg()
            ))) {
                Sys.err("Join Class", "Unable to send request.");
                Global.guiAdapter.makeEvent("joinClass", "Unable to send request");
            }
        } else {
            Sys.err("Join Class", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("joinClass", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Join Class: on invalid result.");
            Global.guiAdapter.makeEvent("joinClass", "Unknown error");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Join Class", "Successful operation.");
            Global.guiAdapter.makeEvent("joinClass", "OK");
            if (!AuthManager.updateUserInfo()) {
                Sys.info("Alter User", "Problem occurred while refreshing user.");
                AuthManager.bringOffline();
            }
        } else {
            Sys.errF("Join Class", "Error: %s.", results);
            Global.guiAdapter.makeEvent("joinClass", results);
        }
        // TODO: GUI process
        return 0;
    }
}
