package top.chorg.kernel.communication.auth;

import top.chorg.kernel.cmd.privateResponders.Login;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.api.auth.User;
import top.chorg.system.Sys;

public class AuthManager {
    private static User user = null;

    /**
     * Complete the authentication and save the user information.
     *
     * @param user The user information variable.
     */
    public static void completeAuth(User user) {
        AuthManager.user = user;
        Sys.infoF("Login", "Login successful. Welcome, %s.", user.getUsername());
        Login.dropTimer();
    }

    public static boolean isOnline() {
        return !(user == null) && HostManager.isConnected("CmdHost");
    }

    public static void bringOffline() {

    }

    public static boolean updateUserInfo() {
        return false;
    }

    public static User getUser() {
        return user;
    }

}