package top.chorg.Kernel.Communication.Net;

import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Support.SerializeUtils;
import top.chorg.System.Sys;

import java.io.IOException;
import java.util.Objects;

public class NetSender {
    public boolean send(Message msg) {
        if (!HostManager.isConnected("CmdHost")) {
            Sys.err("Sender", "Host not connected yet!");
            return false;
        }
        try {
            if (HostManager.isConnected("CmdHost")) {
                Objects.requireNonNull(
                        HostManager.getPrintWriter("CmdHost")).println(SerializeUtils.serialize(msg)
                );
            }
        } catch (IOException e) {
            Sys.err("Sender", "Error while sending data.");
            return false;
        }
        return true;
    }
}
