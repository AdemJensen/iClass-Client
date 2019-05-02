package top.chorg.kernel.cmd.privateResponders.file;

import com.google.gson.JsonSyntaxException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.file.FileInfo;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

/**
 * args: classId. if id = -1, then return self list. if id = 0, return public list.
 */
public class FetchList extends CmdResponder {

    public FetchList(String... args) {
        super(args);
    }

    /**
     * The args are:
     * String, "published" / "all"
     *
     * @return The status
     * @throws IndexOutOfBoundsException When not enough args.
     */
    @Override
    public int response() throws IndexOutOfBoundsException {
        if (!hasNextArg()) {
            Sys.err(
                    "Fetch File List",
                    "Fatal error while invoking private methods: not enough args."
            );
            Sys.exit(30);
        }
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchFileList",
                    nextArg()
            ))) {
                Sys.err("Fetch File List", "Unable to send request.");
                Global.guiAdapter.makeEvent("fetchFileList", "Unable to send request");
            }
        } else {
            Sys.err("Fetch File List", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("fetchFileList", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        FileInfo[] results;
        String arg = nextArg();
        try {
            results = Global.gson.fromJson(arg, FileInfo[].class);
            if (results == null) throw new JsonSyntaxException("E");
        } catch (JsonSyntaxException e) {
            Sys.errF("Fetch File List List", "Error: %s.", arg);
            Global.guiAdapter.makeEvent("fetchFileList", arg);
            Global.dropVar("FILE_LIST_INTERNAL");
            return 8;
        }
        if (Global.varExists("FILE_LIST_INTERNAL")) {
            Global.setVar("FILE_LIST_CACHE", results);
            Global.dropVar("FILE_LIST_INTERNAL");
            return 0;
        }
        Global.guiAdapter.makeEvent("fetchFileList", arg);
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%5s|%20s|%10s|%20s|%10s|%10s|%15s\n",
                "id", "name", "uploader", "date", "class", "level", "size"
        );
        for (FileInfo result : results) {
            Sys.cmdLinePrintF(
                    "%5s|%20s|%10s|%20s|%10s|%10s|%15s\n",
                    result.id, result.name, result.uploader,
                    result.date, result.classId, result.level, result.size.toString()
            );
        }
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
