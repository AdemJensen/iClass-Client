package top.chorg.kernel.cmd.privateResponders.auth;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.support.MD5;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class ChangePassword extends CmdResponder {
    public ChangePassword(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "changePassword",
                    Global.gson.toJson(
                            new String[]{ MD5.encode(nextArg()), MD5.encode(nextArg()) } // old password, new password
                    )
            ))) {
                Sys.err("Change Password", "Unable to send request.");
                Global.guiAdapter.makeEvent("changePassword", "Unable to send request");
            }
        } else {
            Sys.err("Change Password", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("changePassword", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Change Password: on invalid result.");
            Global.guiAdapter.makeEvent("changePassword", "Unknown error");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Change Password", "Successful operation.");
            Global.guiAdapter.makeEvent("changePassword", "OK");
            Sys.info("Change Password", "you need to login again.");
            AuthManager.bringOffline();
        } else {
            Sys.errF("Change Password", "Error: %s.", results);
            Global.guiAdapter.makeEvent("changePassword", results);
        }
        // TODO: GUI process
        return 0;
    }
}
