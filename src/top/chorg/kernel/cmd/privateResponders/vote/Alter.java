package top.chorg.kernel.cmd.privateResponders.vote;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.vote.AlterRequest;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.support.DateTime;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class Alter extends CmdResponder {

    public Alter(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "alterVote",
                    Global.gson.toJson(new AlterRequest(
                            Objects.requireNonNull(nextArg(int.class)),
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
                Sys.err("Alter Vote", "Unable to send request.");
                Global.guiAdapter.makeEvent("judgeOnline", "Unable to send request");
            }
        } else {
            Sys.err("Alter Vote", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("judgeOnline", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Alter Vote: on invalid result.");
            Global.guiAdapter.makeEvent("judgeOnline", "Unknown error");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Alter Vote", "Successful operation.");
            Global.guiAdapter.makeEvent("judgeOnline", "OK");
        } else {
            Sys.errF("Alter Vote", "Error: %s.", results);
            Global.guiAdapter.makeEvent("judgeOnline", results);
        }
        // TODO: GUI process
        return 0;
    }
}
