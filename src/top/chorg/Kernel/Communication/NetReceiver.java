package top.chorg.Kernel.Communication;

import top.chorg.System.Global;

import java.io.IOException;

/**
 * To process the messages coming from server side.
 */
public class NetReceiver extends Thread {
    public void run() {
        try {
            while (true) {
                String msg = Global.bufferedReader.readLine();
                if (msg == null) {
                    Global.socket.close();
                    System.out.println("[Notice] Connection failure. Server closed.");
                    System.exit(0);
                }
                System.out.println(msg);
            }
        } catch (IOException e) {
            System.out.println("[Notice] The connection has been closed.");
        }
    }
}
