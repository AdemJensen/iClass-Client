package top.chorg.kernel.cmd.publicResponders;

import top.chorg.kernel.cmd.CmdManager;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.support.MD5;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class Register extends CmdResponder {

    public Register(String...args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (argAmount() < 2) {
            Sys.warn("Register", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Register", "For more help, please type 'help register' for more help.");
            return 206;
        }
        CmdManager privateMan = Global.cmdManPrivate;
        CmdResponder resp =  Objects.requireNonNull(privateMan).execute("register", nextArg(), nextArg());
        while (resp.isAlive()) { }
        return resp.getReturnVal();
    }

    @Override
    public String getManual() {
        return "Register a new account from server. Usage: register [username] [password].";
    }
}
