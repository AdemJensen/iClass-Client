package top.chorg.kernel.cmd.privateResponders.auth;

import com.google.gson.JsonSyntaxException;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.auth.AuthInfo;
import top.chorg.kernel.communication.api.auth.AuthResult;
import top.chorg.kernel.communication.api.auth.User;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.kernel.communication.net.NetReceiver;
import top.chorg.kernel.communication.net.NetSender;
import top.chorg.support.MD5;
import top.chorg.support.Timer;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class Login extends CmdResponder {

    public Login(String...args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (Global.varExists("AUTH_TIMER")) {
            Sys.err("Login", "Ongoing auth-relevant action in progress, please retry later.");
            Global.guiAdapter.makeEvent("loginResult", "Ongoing auth-relevant action");
            return 208;
        }
        if (HostManager.isConnected("CmdHost")) {
            Sys.err("Login", "User already online, please log off first.");
            Global.guiAdapter.makeEvent("loginResult", "User already online");
            return 205;
        }
        Sys.info("Login", "Attempting to login.");
        Global.setVar("AUTH_TIMER", new Timer(10000, (Object[] args) -> {
            int res = clearTimer();
            if (res != 0) Sys.err("Login", "Timed out while sending authentication info (207).");
            Global.guiAdapter.makeEvent("loginResult", "Timed out while sending information (207).");
            return res;
        }));
        if (HostManager.connect(
                "CmdHost",
                Global.conf.Cmd_Server_Host,
                Global.conf.Cmd_Server_Port
        ) != 0) {
            dropTimer();
            Global.guiAdapter.makeEvent("loginResult", "Host not online");
            return 209;
        }
        Global.masterSender = new NetSender("CmdHost");
        Global.masterReceiver = new NetReceiver("CmdHost");
        Global.masterReceiver.start();
        AuthInfo authInfo = null;
        String cmdName = nextArg();
        if (cmdName.equals("Normal")) {
            authInfo = new AuthInfo(nextArg(), MD5.encode(nextArg()));
        } else if (cmdName.equals("Token")) {
            authInfo = new AuthInfo(nextArg());
        } else {
            Sys.err("Login", "Invalid login info.");
            Sys.exit(21);
        }
        if (!Global.masterSender.send(new Message("login", Global.gson.toJson(authInfo)))) {
            Sys.err("Login", "Unable to send authentication info (206).");
            Global.guiAdapter.makeEvent("loginResult", "Unable to send authentication info (206).");
            HostManager.disconnect("CmdHost");
            dropTimer();
            return 206;
        }
        while (Global.getVar("AUTH_TIMER") != null) { }
        return 0;
    }

    public static void dropTimer() {
        if (Global.varExists("AUTH_TIMER")) {
            Global.getVarCon("AUTH_TIMER", Timer.class).stop();
            clearTimer();
        }
    }

    public static int clearTimer() {
        Global.dropVar("AUTH_TIMER");
        if (AuthManager.isOnline()) return 0;
        else {
            HostManager.disconnect("CmdHost");
            return 207;
        }
    }

    @Override
    public int onReceiveNetMsg() {
        AuthResult res;
        try {
            res = nextArg(AuthResult.class);
        } catch(JsonSyntaxException e) {
            HostManager.onInvalidTransmission("Invalid message content (1)");
            return 1;
        }
        try {
            if (res != null) {
                switch (res.result) {
                    case "Granted":
                        User content = Global.gson.fromJson(res.obj, User.class);
                        AuthManager.completeAuth(content);
                        Global.guiAdapter.makeEvent("loginResult", "OK");
                        return 0;
                    case "Denied":
                        Sys.errF("Login", "Access denied : (%s)", res.obj);
                        Global.guiAdapter.makeEvent("loginResult", res.obj);
                        dropTimer();
                        return 6;
                    default:
                        HostManager.onInvalidTransmission("Invalid message content (2)");
                        return 2;
                }
            } else {
                HostManager.onInvalidTransmission("Invalid message content (2)");
                return 2;
            }
        } catch (ClassCastException | JsonSyntaxException e) {
            HostManager.onInvalidTransmission("Invalid message content (3)");
            dropTimer();
            return 3;
        } catch (Exception e) {
            HostManager.onInvalidTransmission("Unknown error (254)");
            Global.guiAdapter.makeEvent("loginResult", "Unknown error (254)");
            dropTimer();
            return 254;
        }
    }

}
