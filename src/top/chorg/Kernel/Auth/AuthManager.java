package top.chorg.Kernel.Auth;

import top.chorg.Kernel.Communication.Connector;
import top.chorg.System.Global;

public class AuthManager {

    public static boolean isConnected() {
        return Connector.isConnected();
    }

    public static User getUser() {
        return (User) Global.getVar("AUTH_USER");
    }

}
