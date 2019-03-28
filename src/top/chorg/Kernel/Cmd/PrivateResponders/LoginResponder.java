package top.chorg.Kernel.Cmd.PrivateResponders;

import top.chorg.Kernel.Cmd.CmdResponder;
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

public class LoginResponder extends CmdResponder {

    public LoginResponder(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        if (Global.varExists("AUTH_TIMER")) {
            Sys.err("Login", "Ongoing Authentication action in process, please retry later.");
            return 208;
        }
        if (HostManager.isConnected("CmdHost") && HostManager.isConnected("FileHost")) {
            Sys.err("Login", "User already online, please log off first.");
            return 205;
        }
        Sys.info("Login", "Attempting to login.");
        HostManager.connect(
                "CmdHost",
                (String) Global.getConfig("Cmd_Server_Host"),
                (int) Global.getConfig("Cmd_Server_Port")
        );
        NetSender cmdSender = new NetSender("CmdHost");
        NetReceiver cmdReceiver = new NetReceiver("CmdHost");
        Global.setVar("NET_CMD_SENDER", cmdSender);
        Global.setVar("NET_CMD_RECEIVER", cmdReceiver);
        cmdReceiver.start();
        if (!cmdSender.send(new Message("login", (SerializableMap) args))) {    // Send auth info
            Sys.err("Login", "Unable to send authentication info (206).");
            HostManager.disconnect("CmdHost");
            return 206;
        }
        Global.setVar("AUTH_STEP", 1);
        Global.setVar("AUTH_TIMER", new Timer(5000, (Object[] args) -> {
            if (AuthManager.isOnline()) return 0;
            else {
                Global.dropVar("AUTH_STEP");
                Global.dropVar("AUTH_TIMER");
                HostManager.disconnect("CmdHost");
                HostManager.disconnect("FileHost");
                Sys.err("Login", "Unable to send authentication info (207).");
                return 207;
            }
        }));
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        return 0;
    }

    @Override
    public String getManual() {
        return null;
    }

}
