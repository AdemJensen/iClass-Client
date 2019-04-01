package top.chorg.Kernel.Cmd.PrivateResponders;

import top.chorg.Kernel.Cmd.CmdResponder;
import top.chorg.Kernel.Communication.Auth.User;
import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Kernel.Communication.Auth.AuthManager;
import top.chorg.Kernel.Communication.Net.NetReceiver;
import top.chorg.Kernel.Communication.Net.NetSender;
import top.chorg.Support.SerializableMap;
import top.chorg.Support.Timer;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.Serializable;
import java.util.Objects;

public class LoginResponder extends CmdResponder {

    public LoginResponder(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        if (Global.varExists("AUTH_TIMER")) {
            Sys.err("Login", "Ongoing Authentication action in progress, please retry later.");
            return 208;
        }
        if (HostManager.isConnected("CmdHost")) {
            Sys.err("Login", "User already online, please log off first.");
            return 205;
        }
        Sys.info("Login", "Attempting to login.");
        Global.setVar("AUTH_TIMER", new Timer(10000, (Object[] args) -> {
            int res = clearTimer();
            if (res != 0) Sys.err("Login", "Timed out while sending authentication info (207).");
            return res;
        }));
        if (HostManager.connect(
                "CmdHost",
                (String) Global.getConfig("Cmd_Server_Host"),
                (int) Global.getConfig("Cmd_Server_Port")
        ) != 0) {
            dropTimer();
            return 209;
        }
        Global.masterSender = new NetSender("CmdHost");
        Global.masterReceiver = new NetReceiver("CmdHost");
        Global.masterReceiver.start();
        if (!Global.masterSender.send(new Message("login", (SerializableMap) args))) {    // Send auth info
            Sys.err("Login", "Unable to send authentication info (206).");
            HostManager.disconnect("CmdHost");
            dropTimer();
            return 206;
        }
        return 0;
    }

    public static void dropTimer() {
        if (Global.varExists("AUTH_TIMER")) {
            ((Timer) Objects.requireNonNull(Global.getVar("AUTH_TIMER"))).stop();
            clearTimer();
        }
    }

    public static int clearTimer() {
        if (AuthManager.isOnline()) return 0;
        else {
            Global.dropVar("AUTH_TIMER");
            HostManager.disconnect("CmdHost");
            return 207;
        }
    }

    @Override
    public int onReceiveNetMsg() {
        SerializableMap resContent;
        try {
            resContent = (SerializableMap) args;
        } catch(ClassCastException e) {
            HostManager.onInvalidTransmission("Invalid message content (1)");
            return 1;
        }
        if (!resContent.containsKey("result") || !resContent.containsKey("obj")) {
            HostManager.onInvalidTransmission("Invalid message content (2)");
            return 2;
        }
        try {
            switch ((String) resContent.get("result")) {
                case "Granted":
                    SerializableMap content = (SerializableMap) resContent.get("obj");
                    if (!content.containsKey("host") || !content.containsKey("port") ||
                            !content.containsKey("user")) {
                        HostManager.onInvalidTransmission("Invalid message content (5)");
                        return 5;
                    }
                    Global.setVar("FILE_SERVER_HOST", content.get("host"));
                    Global.setVar("FILE_SERVER_PORT", content.get("port"));
                    AuthManager.completeAuth((User) content.get("user"));
                    return 0;
                case "Denied":
                    Sys.err("Login", "Access denied while connecting to auth host!");
                    dropTimer();
                    return 6;
                default:
                    HostManager.onInvalidTransmission("Invalid message content (4)");
                    return 4;
            }
        } catch (ClassCastException e) {
            HostManager.onInvalidTransmission("Invalid message content (3)");
            dropTimer();
            return 3;
        } catch (Exception e) {
            HostManager.onInvalidTransmission("Unknown error (254)");
            dropTimer();
            return 254;
        }
    }

}
