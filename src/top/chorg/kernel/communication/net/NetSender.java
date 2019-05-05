package top.chorg.kernel.communication.net;

import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.io.PrintWriter;

public class NetSender {
    private String identifier;
    private PrintWriter printWriter;

    public NetSender(String identifier) {
        this.identifier = identifier;
        this.printWriter = HostManager.getPrintWriter(identifier);
    }

    public boolean send(Message msg) {
        if (HostManager.isConnected(identifier)) {
            printWriter.println(Global.gson.toJson(msg));
            printWriter.flush();
            return true;
        }
        else {
            Sys.errF("Sender", "Host (%s) not connected yet!", identifier);
            return false;
        }
    }

    public void close() {
        printWriter.close();
    }
}
