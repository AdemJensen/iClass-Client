package top.chorg.kernel.cmd.publicResponders.auth;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class Classes extends CmdResponder {

    public Classes(String...args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            String cmd = nextArg();
            CmdResponder resp;
            switch (cmd) {
                case "join":
                    resp = joinClass();
                    break;
                case "exit":
                    resp = exitClass();
                    break;
                case "inspect":
                    resp = inspectClass();
                    break;
                case "online":
                    resp = getOnline();
                    break;
                default:
                    Sys.errF("Class", "Cmd '%s' is invalid.", cmd);
                    return 1;
            }
            if (resp == null) {
                Sys.err("Class", "Something went wrong when executing commands.");
                return 2;
            } else {
                while (resp.isAlive()) {}
            }
        } else {
            Sys.err("Class", "User is not online, please login first.");
        }
        return 0;
    }


    public CmdResponder joinClass() {
        if (argAmount() < 1) {
            Sys.warn("Join Class", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Join Class", "For more help, please type 'help join' for more help.");
            return null;
        }
        String req = nextArg();
        int uu;
        try {
            uu = Integer.parseInt(req);
        } catch (NumberFormatException e) {
            Sys.err("Join Class", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("joinClass", String.valueOf(uu));
    }

    public CmdResponder exitClass() {
        if (argAmount() < 1) {
            Sys.warn("Exit Class", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Exit Class", "For more help, please type 'help join' for more help.");
            return null;
        }
        String req = nextArg();
        int uu;
        try {
            uu = Integer.parseInt(req);
        } catch (NumberFormatException e) {
            Sys.err("Exit Class", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("exitClass", String.valueOf(uu));
    }

    public CmdResponder inspectClass() {
        if (argAmount() < 1) {
            Sys.warn("Inspect Class", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Inspect Class", "For more help, please type 'help join' for more help.");
            return null;
        }
        String req = nextArg();
        int uu;
        try {
            uu = Integer.parseInt(req);
        } catch (NumberFormatException e) {
            Sys.err("Inspect Class", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("fetchClassInfo", String.valueOf(uu));
    }

    public CmdResponder getOnline() {
        if (argAmount() < 1) {
            Sys.warn("Get Online Users", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Get Online Users", "For more help, please type 'help join' for more help.");
            return null;
        }
        String req = nextArg();
        int uu;
        try {
            uu = Integer.parseInt(req);
        } catch (NumberFormatException e) {
            Sys.err("Get Online Users", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("fetchOnline", String.valueOf(uu));
    }

    @Override
    public String getManual() {
        return "Join a class. Usage: join [classId].";
    }

}
