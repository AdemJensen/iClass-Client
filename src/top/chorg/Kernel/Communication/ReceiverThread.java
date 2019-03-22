package top.chorg.Kernel.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiverThread extends Thread {
    Socket s;
    PrintWriter pw;
    BufferedReader bfr;
    public ReceiverThread(Socket s, PrintWriter pw, BufferedReader bfr) {
        this.s = s;
        this.pw = pw;
        this.bfr = bfr;
    }
    public void run() {
        try {
            while (true) {
                String msg = bfr.readLine();
                if (msg == null) {
                    s.close();
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
