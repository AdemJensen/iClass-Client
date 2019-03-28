package top.chorg.Kernel.Communication.Net;

import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Support.SerializeUtils;
import top.chorg.System.Sys;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * To process the messages coming from server side.
 */
public class NetReceiver extends Thread {
    String identifier;
    BufferedReader bufferedReader;

    public NetReceiver(String identifier) {
        this.identifier = identifier;
        this.bufferedReader = HostManager.getBufferedReader(identifier);
    }

    public void run() {

        try {
            while (true) {
                String msg = bufferedReader.readLine();
                if (msg == null) {
                    HostManager.disconnect(identifier);
                    Sys.warnF(
                            "Net",
                            "Host (%s) connection lost.",
                            identifier
                    );
                    break;
                }
                try {
                    NetManager.execute((Message) SerializeUtils.deserialize(msg));
                } catch (ClassNotFoundException e) {
                    Sys.warnF(
                            "Net",
                            "Connection (%s) has been closed.",
                            identifier
                    );
                }
            }
        } catch (IOException e) {
            Sys.warnF(
                    "Net",
                    "The connection (%s) has been closed.",
                    identifier
            );
        }
    }
}
