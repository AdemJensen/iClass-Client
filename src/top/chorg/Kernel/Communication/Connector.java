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
                Global.socket = new Socket(host, port);
                Global.printWriter = new PrintWriter(new OutputStreamWriter(Global.socket.getOutputStream()));
                Global.bufferedReader = new BufferedReader(new InputStreamReader(Global.socket.getInputStream()));
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
        if (Global.socket == null) {
            Sys.err(
                    "Net",
                    "Connection was already lost or never established!"
            );
            Sys.exit(11);
        }
        try {
            Global.socket.close();
        } catch (IOException e) {
            Sys.err(
                    "Net",
                    "Unable to close socket connection!"
            );
            Sys.exit(12);
        }
        Sys.info(
                "Net",
                "Remote host connection terminated."
        );
    }

    public static void clearSocket() {
        Global.socket = null;
        Global.printWriter = null;
        Global.bufferedReader = null;
    }
}
