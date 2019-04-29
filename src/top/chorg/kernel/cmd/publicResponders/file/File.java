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
                case "ls":
                    resp = listFile();
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
        try {
            int fileId = Objects.requireNonNull(nextArg(int.class));
            return Global.cmdManPrivate.execute(
                    "fetchFileInfo",
                    String.valueOf(fileId)
            );
        } catch (NullPointerException e) {
            Sys.err("Inspect File Info", "Too few arguments, type 'help' for more info.");
            return null;
        }
    }

    private CmdResponder listFile() {
        String operation = nextArg();
        if (operation.equals("self")) operation = "-1";
        try {
            int classId = Integer.parseInt(operation);
            return Global.cmdManPrivate.execute(
                    "fetchFileList",
                    String.valueOf(classId)
            );
        } catch (NullPointerException | NumberFormatException e) {
            Sys.err("Fetch File List", "Invalid arguments, type 'help' for more info.");
            return null;
        }
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
                "\t\t- ls [classId]\t\tList all the files in class that you can access.\n\t\t\t" +
                "If you want to list all your uploaded files, just type 'ls self\n" +
                "\t\t- upload [path] [classId] (level)\t\tUpload specific file.\n\t\t\t" +
                "If you want to upload them to a public area, just make classId = 0.\n\t\t\t" +
                "If level leave blank, it will fill '0' for you." +
                "\t\t- download [fileId]\t\tDownload targeted file.\n" +
                "\t\t- inspect [fileId]\t\tShow the information of a targeted file.\n";
    }
}
