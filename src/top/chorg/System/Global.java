package top.chorg.System;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Global {
    public static Var glob = new Var();
    public static boolean GUI_MODE = true;
    public static boolean DEV_MODE = false;

    public static boolean GUI_MODE_MODIFIED = false;
    public static boolean DEV_MODE_MODIFIED = false;

    public static Socket socket;
    public static PrintWriter printWriter;
    public static BufferedReader bufferedReader;

}
