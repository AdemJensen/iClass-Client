package top.chorg.Kernel.Communication.Net;

import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Support.SerializeUtils;
import top.chorg.System.Sys;

import java.io.IOException;
import java.io.PrintWriter;

public class NetSender {
    String identifier;
    PrintWriter printWriter;

    public NetSender(String identifier) {
        this.identifier = identifier;
        this.printWriter = HostManager.getPrintWriter(identifier);
    }

    public boolean send(Message msg) {
        if (!HostManager.isConnected(identifier)) {
            Sys.errF("Sender", "Host (%s) not connected yet!", identifier);
            return false;
        }
        try {
            if (HostManager.isConnected(identifier)) {
                printWriter.println(SerializeUtils.serialize(msg));
            }
        } catch (IOException e) {
            Sys.errF("Sender", "Error while sending data (%s).", identifier);
            return false;
        }
        return true;
    }
}
