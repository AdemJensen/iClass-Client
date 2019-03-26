package top.chorg.CmdLine;

import top.chorg.Kernel.Communication.Message;
import top.chorg.Kernel.Managers.CmdManager;
import top.chorg.Kernel.Managers.CmdResponder;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.util.Scanner;

public class CmdLineAdapter {
    public static void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to iClass.");
        System.out.printf("System running under command line mode (Ver %s).\n", Global.getVar("VERSION"));
        while (true) {
            System.out.printf("%s >>> ", "[Offline]");    // TODO: User auth check
            String cmd = sc.nextLine();
            if (cmd.equals("exit")) break;

            String[] args = cmd.split(" ");
            if (!CmdManager.cmdExists(args[0])) {
                Sys.errF("CMD", "Command '%s' not found.", args[0]);
                continue;
            }

            Message msg = new Message();
            msg.msgType = args[0];
            String[] content = new String[args.length - 1];
            System.arraycopy(args, 1, content, 0, args.length - 1);
            msg.content = content;
            CmdResponder responderObj = CmdManager.execute(msg);

            if (responderObj == null) {
                Sys.err("Cmd Line", "Responder error: Unable to create responder instance.");
                Sys.exit(14);
            } else {
                while (!responderObj.isDone());
            }
        }
        Sys.info("Cmd Line", "Command line is closing now.");
    }
}
