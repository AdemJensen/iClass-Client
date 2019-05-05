package top.chorg.kernel.cmd.privateResponders.vote;

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
                    "delVote",
                    nextArg()
            ))) {
                Sys.err("Delete Vote", "Unable to send request.");
                Global.guiAdapter.makeEvent("delVote", "Unable to send request");
            }
        } else {
            Sys.err("Delete Vote", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("delVote", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Delete Vote: on invalid result.");
            Global.guiAdapter.makeEvent("delVote", "Unknown error");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Delete Vote", "Successful operation.");
            Global.guiAdapter.makeEvent("delVote", "OK");
        } else {
            Sys.errF("Delete Vote", "Error: %s.", results);
            Global.guiAdapter.makeEvent("delVote", results);
        }
        // TODO: GUI process
        return 0;
    }
}