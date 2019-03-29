package top.chorg.Kernel.Communication.Net.Responders.Auth;

import top.chorg.Kernel.Communication.Auth.AuthManager;
import top.chorg.Kernel.Communication.Auth.User;
import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Kernel.Communication.Net.NetReceiver;
import top.chorg.Kernel.Communication.Net.NetResponder;
import top.chorg.Kernel.Communication.Net.NetSender;
import top.chorg.Support.SerializableMap;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.Serializable;

public class ContinueLogin extends NetResponder {

    public ContinueLogin(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        SerializableMap resContent;
        try {
            resContent = (SerializableMap) args;
        } catch(ClassCastException e) {
            HostManager.onInvalidTransmission("Invalid message content (1)");
            return 1;
        }
        if (!resContent.containsKey("step") || !resContent.containsKey("obj")) {
            HostManager.onInvalidTransmission("Invalid message content (2)");
            return 2;
        }
        try {
            switch ((int) resContent.get("step")) {
                case 1:
                    SerializableMap content = (SerializableMap) resContent.get("obj");
                    if (!content.containsKey("a") || !content.containsKey("p")) {
                        HostManager.onInvalidTransmission("Invalid message content (5)");
                        return 5;
                    }
                    return stepOne(content);
                case 2:
                    return stepTwo((User) resContent.get("obj"));
                default:
                    HostManager.onInvalidTransmission("Invalid message content (4)");
                    return 4;
            }
        } catch (ClassCastException e) {
            HostManager.onInvalidTransmission("Invalid message content (3)");
            return 3;
        } catch (Exception e) {
            HostManager.onInvalidTransmission("Unknown error (254)");
            return 254;
        }
    }

    private int stepOne(SerializableMap resContent) {
        try {
            if (!Global.varExists("AUTH_STEP") || ((int) Global.getVar("AUTH_STEP")) != 1) {
                HostManager.onInvalidTransmission("Auth step mismatch");
                return 1;
            }
            HostManager.connect(
                    "FileHost",
                    (String) resContent.get("a"),
                    (int) resContent.get("p")
            );
            NetSender fileSender = new NetSender("FileHost");
            NetReceiver fileReceiver = new NetReceiver("FileHost");
            Global.setVar("NET_FILE_SENDER", fileSender);
            Global.setVar("NET_FILE_RECEIVER", fileReceiver);
            fileReceiver.start();
            if (fileSender.send(new Message("connect", new SerializableMap(
                    "id", resContent.get("id")
            )))) {
                Global.setVar("AUTH_STEP", 2);
                return 0;
            } else {      // Send auth info
                Sys.err("Login", "Unable to send authentication info (206).");
                HostManager.disconnect("CmdHost");
                HostManager.disconnect("FileHost");
                return 206;
            }
        } catch (ClassCastException e) {
            HostManager.onInvalidTransmission("Invalid message content (3)");
            return 3;
        } catch (Exception e) {
            HostManager.onInvalidTransmission("Unknown error (254)");
            return 254;
        }

    }

    private int stepTwo(User args) {
        if (!Global.varExists("AUTH_STEP") || ((int) Global.getVar("AUTH_STEP")) != 2) {
            Sys.warn(
                    "Auth",
                    "Received invalid response, ignoring."
            );
            return 1;
        }
        User resContent;
        try {
            resContent = args;
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
