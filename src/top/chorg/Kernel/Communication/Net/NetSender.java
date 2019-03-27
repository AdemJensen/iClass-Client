package top.chorg.Kernel.Communication.Net;

import top.chorg.Kernel.Communication.Connector;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Support.SerializeUtils;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.IOException;
import java.io.PrintWriter;

public class NetSender {
    public boolean send(Message msg) {
        if (!Connector.isConnected()) {
            Sys.err("Sender", "Socket not connected yet!");
            return false;
        }
        try {
            ((PrintWriter) Global.getVar("printWriter")).println(SerializeUtils.serialize(msg));
        } catch (IOException e) {
            Sys.err("Sender", "Error while sending data.");
            return false;
        }
        return true;
    }
}
