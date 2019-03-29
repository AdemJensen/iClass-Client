package top.chorg.Kernel.Communication.Net.Responders.Auth;

import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Kernel.Communication.Net.NetReceiver;
import top.chorg.Kernel.Communication.Net.NetResponder;
import top.chorg.Kernel.Communication.Net.NetSender;
import top.chorg.Support.SerializableMap;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.Serializable;

public class ContinueLoginStep1 extends NetResponder {

    public ContinueLoginStep1(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        if (!Global.varExists("AUTH_STEP") || ((int) Global.getVar("AUTH_STEP")) != 1) {
            HostManager.onInvalidTransmission("Auth step mismatch");
            return 1;
        }
        SerializableMap resContent;
        try {
            resContent = (SerializableMap) args;
        } catch(ClassCastException e) {
            HostManager.onInvalidTransmission("Invalid message content");
            return 1;
        }
        if (!resContent.containsKey("a") || !resContent.containsKey("p")) {
            HostManager.onInvalidTransmission("Invalid message content");
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
                "id", (int) resContent.get("id")
        )))) {
            Global.setVar("AUTH_STEP", 2);
            return 0;
        } else {      // Send auth info
            Sys.err("Login", "Unable to send authentication info (206).");
            HostManager.disconnect("CmdHost");
            HostManager.disconnect("FileHost");
            return 206;
        }
    }
}
