package top.chorg.cmdLine;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Scanner;

public class CmdLineAdapter {
    public static void start() {
        Scanner sc = new Scanner(System.in);
        Sys.clearLine();
        System.out.println("Welcome to iClass Server.");
        System.out.printf(
                "System running under command line mode (Ver %s).\n",
                Global.getVarCon("VERSION", String.class)
        );
        outputDecoration();
        while (true) {
            String cmd = sc.nextLine();
            if (cmd.equals("exit")) break;
            String[] args = cmd.split(" ");
            if (args.length == 0 || args[0].length() == 0) {
                outputDecoration();
                continue;
            }
            if (!Global.cmdManPublic.cmdExists(args[0])) {
                Sys.errF("CMD", "Command '%s' not found.", args[0]);
                continue;
            }
            CmdResponder responderObj = Global.cmdManPublic.execute(args);

            if (responderObj == null) {
                Sys.err("Cmd Line", "Responder error: Unable to create responder instance.");
                Sys.exit(14);
            } else {
                while (responderObj.isAlive());
            }
        }
        Global.cmdManPublic.execute("stop");
    }

    public static void outputDecoration() {
        System.out.printf("[%s] >>> ", AuthManager.isOnline() ? AuthManager.getUser().getUsername() : " - OFFLINE - ");
    }
}
