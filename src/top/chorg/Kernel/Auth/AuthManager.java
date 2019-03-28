package top.chorg.Kernel.Auth;

import top.chorg.Kernel.Communication.HostManager;
import top.chorg.System.Global;

public class AuthManager {

    public static boolean isConnected() {
        return HostManager.isConnected("CmdHost");
    }

    public static User getUser() {
        return (User) Global.getVar("AUTH_USER");
    }

}
