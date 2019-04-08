package top.chorg.kernel.cmd.publicResponders;

import top.chorg.kernel.cmd.CmdManager;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.support.MD5;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class NormalLogin extends CmdResponder {

    public NormalLogin(String[] args) {
        super(args);
    }

    @Override
    public int response() {
        if (args == null) {
            Sys.err("Auth", "Arguments not assigned.");
            return 204;
        }
        if (args.length < 2) {
            Sys.warn("Auth", "Arguments for login are too few. Needed two parameters.");
            Sys.info("Auth", "For more help, please type 'help login' for more help.");
            return 203;
        }
        CmdManager privateMan = Global.cmdManPrivate;
        CmdResponder resp =  Objects.requireNonNull(privateMan).execute(new String[]{
                "login", "Normal", args[0], MD5.encode(args[1])
        });
        while (resp.isAlive());
        return resp.getReturnVal();
    }

    @Override
    public int onReceiveNetMsg() {
        return 0;
    }

    @Override
    public String getManual() {
        return "To login to the remote host. Usage: login [username] [password].";
    }
}
