package top.chorg.Kernel.Communication;

import top.chorg.System.Sys;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class HostManager {
    private static HashMap<String, Socket> SocketObjs = new HashMap<>();   // Socket object storage
    private static HashMap<String, PrintWriter> pwObjs = new HashMap<>();       // Socket printWriter storage
    private static HashMap<String, BufferedReader> brObjs = new HashMap<>();       // Socket bufferedReader storage

    /**
     * Make a connection to the remote host and store them into an HashMap.
     *
     * @param identifier Defines how you want to invoke this connection.
     * @param host The server address.
     * @param port The server port.
     * @return An int representing the result of connection.
     *  - If = 0: Success, no problem.
     *  - If = 1: Unable to connect.
     *  - If = 2: Identifier already exists.
     */
    public static int connect(String identifier, String host, int port) {
        if (SocketObjs.containsKey(identifier)) {
            Sys.err("Net", "Identifier already exists.");
            return 2;
        }
        try {
            SocketObjs.put(identifier, new Socket(host, port));
            pwObjs.put(
                    identifier,
                    new PrintWriter(new OutputStreamWriter(SocketObjs.get(identifier).getOutputStream()))
            );
            brObjs.put(
                    identifier,
                    new BufferedReader(new InputStreamReader(SocketObjs.get(identifier).getInputStream()))
            );
        } catch (IOException e) {
            Sys.err(
                    "Net",
                    "Connection failure, host might offline or client offline."
            );
            remove(identifier);
            return 1;
        }
        Sys.info(
                "Net",
                "Successfully handshake from remote host."
        );
        return 0;
    }

    public static void disconnect(String identifier) {
        if (!SocketObjs.containsKey(identifier)) return;
        try {
            SocketObjs.get(identifier).close();
        } catch (IOException e) {
            Sys.errF(
                    "Net",
                    "Problems while closing socket connection (%s), force execution start.",
                    identifier
            );
        }
        Sys.infoF(
                "Net",
                "Remote host connection (%s) terminated.",
                identifier
        );
        remove(identifier);
    }

    public static boolean isConnected(String identifier) {
        return SocketObjs.containsKey(identifier) && (SocketObjs.get(identifier).isConnected());
    }

    private static void remove(String identifier) {
        SocketObjs.remove(identifier);
        pwObjs.remove(identifier);
        brObjs.remove(identifier);
    }

    public static PrintWriter getPrintWriter(String identifier) {
        if (isConnected(identifier)) {
            return pwObjs.get(identifier);
        }
        Sys.err(
                "Net",
                "Designated connection not established."
        );
        return null;
    }

    public static BufferedReader getBufferedReader(String identifier) {
        if (isConnected(identifier)) {
            return brObjs.get(identifier);
        }
        Sys.err(
                "Net",
                "Designated connection not established."
        );
        return null;
    }

    public static void onInvalidTransmission(String identifier) {
        Sys.warnF(
                "Net",
                "Received invalid response (%s).",
                identifier
        );
    }

}
