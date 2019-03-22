package top.chorg.Kernel.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SenderThread extends Thread {
    Scanner sc;
    Socket s;
    PrintWriter pw;
    BufferedReader bfr;
    public SenderThread(Scanner sc, Socket s, PrintWriter pw, BufferedReader bfr) {
        this.sc = sc;
        this.s = s;
        this.pw = pw;
        this.bfr = bfr;
    }
    public void run() {
        try {
            while (true) {
                String str = sc.nextLine();
                if (str.equals("EXIT")) {
                    s.close();
                    System.out.println("[Notice] The connection has been closed.");
                    System.exit(0);
                }
                pw.println(str);
                pw.flush();
            }
        } catch (IOException e) {
            System.out.println("[Notice] Connection failure. Server closed.");
        }
    }
}
