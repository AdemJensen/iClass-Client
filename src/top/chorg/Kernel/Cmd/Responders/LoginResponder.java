package top.chorg.Kernel.Cmd.Responders;

import top.chorg.Kernel.Communication.Connector;
import top.chorg.Kernel.Cmd.CmdResponder;
import top.chorg.System.Global;
import top.chorg.System.Sys;

public class LoginResponder extends CmdResponder {

    @Override
    public int response() {
        String[] var = (String[]) args;
        if (var.length < 2) {
            Sys.warn("Auth", "Arguments for login are too few. Needed two parameters.");
            Sys.info("Auth", "For more help, please type 'help login' for more help.");
            return 203;
        }
        boolean connectionResult = Connector.connect(
                (String) Global.getConfig("Socket_Host"),
                (int) Global.getConfig("Socket_Port")
        );
        if (!connectionResult) return 204;

        return 0;
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
