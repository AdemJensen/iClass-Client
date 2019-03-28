package top.chorg.Kernel.Communication.Net.Responders.Auth;

import top.chorg.Kernel.Communication.Auth.AuthManager;
import top.chorg.Kernel.Communication.Auth.User;
import top.chorg.Kernel.Communication.Net.NetResponder;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.Serializable;

public class ContinueLoginStep2 extends NetResponder {

    public ContinueLoginStep2(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        if (!Global.varExists("AUTH_STEP") || ((int) Global.getVar("AUTH_STEP")) != 2) {
            Sys.warn(
                    "Auth",
                    "Received invalid response, ignoring."
            );
            return 1;
        }
        User resContent;
        try {
            resContent = (User) args;
        } catch(ClassCastException e) {
            Sys.warn(
                    "Auth",
                    "Received invalid response, ignoring."
            );
            return 1;
        }
        AuthManager.completeAuth(resContent);
        Global.dropVar("AUTH_STEP");
        return 0;
    }
}
