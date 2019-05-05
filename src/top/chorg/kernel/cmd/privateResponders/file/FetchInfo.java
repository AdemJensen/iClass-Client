package top.chorg.kernel.cmd.privateResponders.file;

import com.google.gson.JsonSyntaxException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.file.FileInfo;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class FetchInfo extends CmdResponder {

    public FetchInfo(String... args) {
        super(args);
    }

    /**
     * The args are:
     * Stringed integer, Vote id.
     *
     * @return The status
     * @throws IndexOutOfBoundsException When not enough args.
     */
    @Override
    public int response() throws IndexOutOfBoundsException {
        if (!hasNextArg()) {
            Sys.err("Fetch File Info", "Fatal error while invoking private methods: not enough args.");
            Sys.exit(30);
        }
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchFileInfo",
                    nextArg()
            ))) {
                Sys.err("Fetch File Info", "Unable to send request.");
                Global.guiAdapter.makeEvent("fetchFileInfo", "Unable to send request");
            }
        } else {
            Sys.err("Fetch File Info", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("fetchFileInfo", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        FileInfo result;
        String arg = nextArg();
        try {
            result = Global.gson.fromJson(arg, FileInfo.class);
        } catch (JsonSyntaxException e) {
            Sys.errF("Fetch File Info", "Error: %s.", arg);
            Global.guiAdapter.makeEvent("fetchFileInfo", arg);
            Global.dropVar("FILE_INFO_INTERNAL");
            return 8;
        }
        if (result == null) {
            HostManager.onInvalidTransmission("Fetch File Info: on invalid result.");
            Global.guiAdapter.makeEvent("fetchFileInfo", "Unknown error");
            Global.dropVar("FILE_INFO_INTERNAL");
            return 1;
        }
        if (Global.varExists("FILE_INFO_INTERNAL")) {
            Global.setVar("FILE_INFO_CACHE", result);
            Global.dropVar("FILE_INFO_INTERNAL");
            return 0;
        }
        Global.guiAdapter.makeEvent("fetchFileInfo", arg);
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%12s | %d\n" +
                        "%12s | %s\n" +
                        "%12s | %d\n" +
                        "%12s | %s\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %s\n",
                "id", result.id,
                "name", result.name,
                "uploader", result.uploader,
                "date", result.date.toString(),
                "classId", result.classId,
                "level", result.level,
                "size", result.size.toString()
        );
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }

}
