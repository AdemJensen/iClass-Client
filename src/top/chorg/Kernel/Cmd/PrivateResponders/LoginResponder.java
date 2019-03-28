package top.chorg.Kernel.Cmd.PrivateResponders;

import top.chorg.Kernel.Cmd.CmdResponder;
import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Support.SerializableMap;
import top.chorg.Support.SerializeUtils;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Objects;

public class LoginResponder extends CmdResponder {

    public LoginResponder(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        if (HostManager.isConnected("CmdHost") && HostManager.isConnected("FileHost")) {
            Sys.err("Auth", "User already online, please log off first.");
            return 208;
        }
        Sys.info("Login", "Attempting to login.");
        HostManager.connect(
                "CmdHost",
                (String) Global.getConfig("Cmd_Server_Host"),
                (int) Global.getConfig("Cmd_Server_Port")
        );
        if (!HostManager.isConnected("CmdHost")) {
            Sys.err("Login", "Unable to login, connection unable to establish (204).");
            HostManager.disconnect("CmdHost");
            return 204;
        }
        PrintWriter cmdPw = HostManager.getPrintWriter("CmdHost");
        BufferedReader cmdBr = HostManager.getBufferedReader("CmdHost");
        try {
            Objects.requireNonNull(cmdPw).println(SerializeUtils.serialize(
                    new Message(
                            "login",
                            (SerializableMap) args
                    )
            ));
            String res = Objects.requireNonNull(cmdBr).readLine();
            if (res == null) {
                Sys.err("Login", "Remote server refused authentication (207).");
                HostManager.disconnect("CmdHost");
                return 207;
            }
            Message resObj = (Message) SerializeUtils.deserialize(res);
            SerializableMap resContent = (SerializableMap) resObj.content;
            if (resObj.msgType.equals("accessGranted")) {
                HostManager.connect(
                        "FileHost",
                        (String) resContent.get("a"),
                        (int) resContent.get("p")
                );

                // TODO: Add User object.
                if (!HostManager.isConnected("FileHost")) {
                    Sys.err("Login", "Unable to login, connection unable to establish (207).");
                    HostManager.disconnect("CmdHost");
                    HostManager.disconnect("FileHost");
                    return 207;
                }
            } else {
                Sys.err("Login", "Remote server connection broken (208).");
                HostManager.disconnect("CmdHost");
                HostManager.disconnect("FileHost");
                return 208;
            }
        } catch (IOException e) {
            Sys.err("Login", "Error while authenticating (205).");
            HostManager.disconnect("CmdHost");
            HostManager.disconnect("FileHost");
            return 205;
        } catch (ClassNotFoundException e) {
            Sys.err("Login", "Error while authenticating (206).");
            HostManager.disconnect("CmdHost");
            HostManager.disconnect("FileHost");
            return 206;
        }
        Sys.info("Login", "You are now online.");
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
