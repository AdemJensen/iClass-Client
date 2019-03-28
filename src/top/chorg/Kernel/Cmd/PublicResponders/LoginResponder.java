package top.chorg.Kernel.Cmd.PublicResponders;

import top.chorg.Kernel.Cmd.CmdManager;
import top.chorg.Kernel.Cmd.CmdResponder;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Support.SerializableMap;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.Serializable;

public class LoginResponder extends CmdResponder {

    public LoginResponder(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        if (args == null) {
            Sys.err("Auth", "Arguments not assigned.");
            return 204;
        }
        String[] var = (String[]) args;
        if (var.length < 2) {
            Sys.warn("Auth", "Arguments for login are too few. Needed two parameters.");
            Sys.info("Auth", "For more help, please type 'help login' for more help.");
            return 203;
        }
        System.out.println("Normal");
        CmdManager privateMan = ((CmdManager) Global.getVar("CMD_MAN_PRIVATE"));
        top.chorg.Kernel.Cmd.CmdResponder resp =  privateMan.execute(new Message(
                "normal",
                new SerializableMap(
                        "username", var[0],
                        "password", var[1]
                )
        ));
        while (!resp.isDone());
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
