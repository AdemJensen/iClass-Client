package top.chorg.Kernel.Communication.Net;

import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 * To process the messages coming from server side.
 */
public class NetReceiver extends Thread {
    public void run() {
        try {
            while (true) {
                String msg = ((BufferedReader) Global.getVar("bufferedReader")).readLine();
                if (msg == null) {
                    ((Socket) Global.getVar("socket")).close();
                    Sys.warn(
                            "Net",
                            "Server connection lost."
                    );
                    break;
                }
                System.out.println(msg);
            }
        } catch (IOException e) {
            Sys.warn(
                    "Net",
                    "The connection has been closed."
            );
        }
    }
}
