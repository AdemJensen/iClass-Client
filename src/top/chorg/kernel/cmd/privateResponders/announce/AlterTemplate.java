package top.chorg.kernel.cmd.privateResponders.announce;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.announcements.AlterTemplateRequest;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class AlterTemplate extends CmdResponder {

    public AlterTemplate(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "alterTemplate",
                    Global.gson.toJson(new AlterTemplateRequest(
                            Objects.requireNonNull(nextArg(int.class)),
                            nextArg(),
                            nextArg(),
                            nextArg()
                    ))
            ))) {
                Sys.err("Alter Template", "Unable to send request.");
                Global.guiAdapter.makeEvent("alterTemplate", "Unable to send request");
            }
        } else {
            Sys.err("Alter Template", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("alterTemplate", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Alter Template: on invalid result.");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Alter Template", "Successful operation.");
            Global.guiAdapter.makeEvent("alterTemplate", "OK");
        } else {
            Sys.errF("Alter Template", "Error: %s.", results);
            Global.guiAdapter.makeEvent("alterTemplate", results);
        }
        // TODO: GUI process
        return 0;
    }
}
