package top.chorg.Kernel.Communication;

import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.*;
import java.net.Socket;

public class Connector {

    public static void connect(String host, int port) {
        int failure = 0;
        while (failure < 3) {
            try {
                Global.setVar("socket", new Socket(host, port));
                Global.setVar(
                        "printWriter",
                        new PrintWriter(new OutputStreamWriter(((Socket) Global.getVar("socket")).getOutputStream()))
                );
                Global.setVar(
                        "bufferedReader",
                        new BufferedReader(new InputStreamReader(((Socket) Global.getVar("socket")).getInputStream()))
                );
            } catch (IOException e) {
                failure++;
                Sys.warnF(
                        "Net",
                        "Connection failure, retrying (%d)",
                        failure
                );
                clearSocket();
                continue;
            }
            break;
        }
        if (failure >= 3) {
            Sys.err(
                    "Net",
                    "Connection failure for too many times, host might offline or client offline."
            );
            Sys.exit(202);
        } else {
            Sys.info(
                    "Net",
                    "Successfully handshake from remote host."
            );
        }
    }

    public static void disconnect() {
        if (!Global.varExists("socket") || !((Socket) Global.getVar("socket")).isConnected()) {
            Sys.warn(
                    "Net",
                    "Connection was already lost or never established!"
            );
            clearSocket();
            return;
        }
        try {
            ((Socket) Global.getVar("socket")).close();
        } catch (IOException e) {
            Sys.err(
                    "Net",
                    "Error while closing socket connection!"
            );
        }
        Sys.info(
                "Net",
                "Remote host connection terminated."
        );
        clearSocket();
    }

    public static void clearSocket() {
        Global.dropVar("socket");
        Global.dropVar("printWriter");
        Global.dropVar("bufferedReader");
    }
}
