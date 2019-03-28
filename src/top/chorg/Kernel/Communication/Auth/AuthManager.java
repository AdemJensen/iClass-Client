package top.chorg.Kernel.Communication.Auth;

import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Support.Timer;
import top.chorg.System.Global;

import java.util.Objects;

public class AuthManager {
    private static User user = null;

    /**
     * Complete the 3rd step of authentication and save the user information.
     *
     * @param user The user information variable.
     */
    public static void completeAuth(User user) {
        ((Timer) Objects.requireNonNull(Global.getVar("AUTH_TIMER"))).stop();
        Global.dropVar("AUTH_TIMER");
        AuthManager.user = user;
    }

    public static boolean isOnline() {
        return !(user == null) &&
                HostManager.isConnected("CmdHost") && HostManager.isConnected("FileHost");
    }

    public static boolean updateUserInfo() {
        return false;
    }

    public static User getUser() {
        return user;
    }

}
