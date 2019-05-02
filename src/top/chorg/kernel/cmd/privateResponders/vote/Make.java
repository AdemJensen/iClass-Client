package top.chorg.kernel.cmd.privateResponders.vote;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.vote.MakeRequest;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class Make extends CmdResponder {

    public Make(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "makeVote",
                    Global.gson.toJson(new MakeRequest(
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int[].class)),
                            hasNextArg() ? nextArg() : ""
                    ))
            ))) {
                Sys.err("Make Vote", "Unable to send request.");
                Global.guiAdapter.makeEvent("makeVote", "Unable to send request");
            }
        } else {
            Sys.err("Make Vote", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("makeVote", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Make Vote: on invalid result.");
            Global.guiAdapter.makeEvent("makeVote", "Unknown error");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Make Vote", "Successful operation.");
            Global.guiAdapter.makeEvent("makeVote", "OK");
        } else {
            Sys.errF("Make Vote", "Error: %s.", results);
            Global.guiAdapter.makeEvent("makeVote", results);
        }
        // TODO: GUI process
        return 0;
    }
}
