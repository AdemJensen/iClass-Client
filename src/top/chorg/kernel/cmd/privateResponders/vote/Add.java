package top.chorg.kernel.cmd.privateResponders.vote;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.vote.AddRequest;
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
                    "addVote",
                    Global.gson.toJson(new AddRequest(
                            nextArg(),
                            nextArg(),
                            nextArg(),
                            new DateTime(nextArg()),
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class))
                    ))
            ))) {
                Sys.err("Add Vote", "Unable to send request.");
                Global.guiAdapter.makeEvent("addVote", "Unable to send request");
            }
        } else {
            Sys.err("Add Vote", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("addVote", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Add Vote: on invalid result.");
            Global.guiAdapter.makeEvent("addVote", "Unknown error");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Add Vote", "Successful operation.");
            Global.guiAdapter.makeEvent("addVote", "OK");
        } else {
            Sys.errF("Add Vote", "Error: %s.", results);
            Global.guiAdapter.makeEvent("addVote", results);
        }
        // TODO: GUI process
        return 0;
    }
}
