package top.chorg.kernel.cmd.privateResponders.announce;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.announcements.AddRequest;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.support.DateTime;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class Add extends CmdResponder {

    public Add(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "addAnnounce",
                    Global.gson.toJson(new AddRequest(
                            nextArg(),
                            nextArg(),
                            new DateTime(nextArg()),
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class))
                    ))
            ))) {
                Sys.err("Add Announce", "Unable to send request.");
            }
        } else {
            Sys.err("Add Announce", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Add Announce: on invalid result.");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Add Announce", "Successful operation.");
        } else {
            Sys.errF("Add Announce", "Error: %s.", results);
        }
        // TODO: GUI process
        return 0;
    }
}
