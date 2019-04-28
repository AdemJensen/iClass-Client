package top.chorg.kernel.cmd.publicResponders.file;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class File extends CmdResponder {

    public File(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            String cmd = nextArg();
            CmdResponder resp;
            switch (cmd) {
                case "upload":
                    resp = uploadFile();
                    break;
                case "inspect":
                    resp = inspectFile();
                    break;
                case "download":
                    resp = downloadFile();
                    break;
                default:
                    Sys.errF("File", "Cmd '%s' is invalid.", cmd);
                    return 1;
            }
            if (resp == null) {
                Sys.err("File", "Something went wrong when executing commands.");
                return 2;
            } else {
                while (resp.isAlive()) {}
            }
        } else {
            Sys.err("File", "User is not online, please login first.");
        }
        return 0;
    }

    private CmdResponder uploadFile() {
        try {
            String path = nextArg();
            int level;
            int classId = Objects.requireNonNull(nextArg(int.class));
            if (hasNextArg()) {
                level = Objects.requireNonNull(nextArg(int.class));
            } else {
                level = 0;
            }
            return Global.cmdManPrivate.execute(
                    "uploadFile",
                    path, String.valueOf(classId), String.valueOf(level)
            );
        } catch (NullPointerException e) {
            Sys.err("Upload File", "Too few arguments, type 'help' for more info.");
            return null;
        }
    }

    private CmdResponder inspectFile() {
        return null;
    }

    private CmdResponder downloadFile() {
        try {
            int fileId = Objects.requireNonNull(nextArg(int.class));
            return Global.cmdManPrivate.execute(
                    "downloadFile",
                    String.valueOf(fileId)
            );
        } catch (NullPointerException e) {
            Sys.err("Download File", "Too few arguments, type 'help' for more info.");
            return null;
        }
    }

    @Override
    public String getManual() {
        return "To make actions that relevant to file system. \n " +
                "\t\t- send [type] [targetId] [content]\t\tSend message to specific target.\n\t\t\t" +
                "If type = 1, then targetId is the classId. If type = 2, then targetId is the userId.\n" +
                "\t\t- history [type] [targetId]\t\tQuery chatting history.\n\t\t\t" +
                "If type = 1, then targetId is the classId. If type = 2, then targetId is the userId.\n" +
                "\t\t- alter\t\tAlter an template. There will be a guidance system guiding you create a template.\n" +
                "\t\t- del [templateId]\t\tDelete an template. Warning: this action is irreversible.";
    }
}
