package top.chorg.kernel.communication.auth;

import top.chorg.kernel.cmd.privateResponders.auth.Login;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.api.auth.User;
import top.chorg.system.Global;
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
        // TODO: GUI
    }

    public static boolean isOnline() {
        return !(user == null) && HostManager.isConnected("CmdHost");
    }

    public static void bringOffline() {
        synchronized (HostManager.class) { // TODO: Might cause unexpected error.
            AuthManager.user = null;
            Global.masterSender.close();
            Global.masterReceiver.close();
            HostManager.disconnect("CmdHost");
        }
        Global.guiAdapter.makeEvent("bringOffline");
    }

    public static boolean updateUserInfo() {
        Global.setVar("USER_INFO_INTERNAL", true);
        Global.cmdManPrivate.execute("fetchUserInfo", String.valueOf(AuthManager.getUser().getId()));
        while (Global.varExists("USER_INFO_INTERNAL")) { }
        user = Global.getVar("USER_INFO_CACHE", User.class);
        Global.dropVar("USER_INFO_CACHE");
        return user != null;
    }

    public static User getUser() {
        return user;
    }

}
