package top.chorg.Kernel.Communication.Auth;

import top.chorg.Kernel.Cmd.PrivateResponders.LoginResponder;
import top.chorg.Kernel.Communication.HostManager;

public class AuthManager {
    private static User user = null;

    /**
     * Complete the authentication and save the user information.
     *
     * @param user The user information variable.
     */
    public static void completeAuth(User user) {
        LoginResponder.dropTimer();
        AuthManager.user = user;
    }

    public static boolean isOnline() {
        return !(user == null) &&
                HostManager.isConnected("CmdHost") && HostManager.isConnected("FileHost");
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
