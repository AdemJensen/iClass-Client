package top.chorg.kernel.cmd.privateResponders.classes;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class Kick extends CmdResponder {
    public Kick(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "kickMember",
                    Global.gson.toJson(new int[]{
                            Objects.requireNonNull(nextArg(int.class)), Objects.requireNonNull(nextArg(int.class))
                    })
            ))) {
                Sys.err("Kick", "Unable to send request.");
                Global.guiAdapter.makeEvent("kickMember", "Unable to send request");
            }
        } else {
            Sys.err("Kick", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("kickMember", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Join Class: on invalid result.");
            Global.guiAdapter.makeEvent("kickMember", "Unknown error");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Kick", "Successful operation.");
            Global.guiAdapter.makeEvent("kickMember", "OK");
            if (!AuthManager.updateUserInfo()) {
                Sys.info("Kick", "Problem occurred while refreshing user.");
                AuthManager.bringOffline();
            }
        } else {
            Sys.errF("Kick", "Error: %s.", results);
            Global.guiAdapter.makeEvent("kickMember", results);
        }
        // TODO: GUI process
        return 0;
    }
}