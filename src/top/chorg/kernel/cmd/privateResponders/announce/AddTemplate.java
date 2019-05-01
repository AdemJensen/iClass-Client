package top.chorg.kernel.cmd.privateResponders.announce;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.announcements.AddTemplateRequest;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class AddTemplate extends CmdResponder {

    public AddTemplate(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "addAnnounceTemplate",
                    Global.gson.toJson(new AddTemplateRequest(
                            nextArg(),
                            nextArg(),
                            nextArg()
                    ))
            ))) {
                Sys.err("Add Template", "Unable to send request.");
                Global.guiAdapter.makeEvent("addTemplate", "Unable to send request");
            }
        } else {
            Sys.err("Add Template", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("addTemplate", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Add Template: on invalid result.");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Add Template", "Successful operation.");
            Global.guiAdapter.makeEvent("addTemplate", "OK");
        } else {
            Sys.errF("Add Template", "Error: %s.", results);
            Global.guiAdapter.makeEvent("addTemplate", results);
        }
        // TODO: GUI process
        return 0;
    }
}
