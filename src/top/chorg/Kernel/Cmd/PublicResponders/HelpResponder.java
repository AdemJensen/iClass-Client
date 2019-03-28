package top.chorg.Kernel.Cmd.PublicResponders;

import top.chorg.Kernel.Cmd.CmdManager;
import top.chorg.Kernel.Cmd.CmdResponder;
import top.chorg.Kernel.Communication.Message;
import top.chorg.System.Global;
import top.chorg.System.Sys;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This responder generates the command line help according to the getManual() declared in the Responders.
 * Notice that this responder will group up the commands that with the same manual content.
 * - If display all: The helper will group up all the commands and display the same manual once.
 * - If display one of them: The helper will display the similar commands below.
 * Attention: If the manual is null, the Helper will:
 *  - If display all: The helper will group up all the commands which doesn't have manual and display them.
 *  - If display one of them: The helper will not display the similar commands.
 * CAUTION: DO NOT return a void string! If you have nothing to say, please return null in the getManual().
 */
public class HelpResponder extends CmdResponder {

    public HelpResponder(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        Object[] vars = (Object[]) args;
        boolean outFirst = false;
        CmdManager cmdMan = (CmdManager) Global.getVar("CMD_MAN_PUBLIC");
        if (vars.length > 0) {
            for (Object var : vars) {
                if (!outFirst) outFirst = true;
                else System.out.println();
                ArrayList<String> keyList = new ArrayList<>(cmdMan.getKeySet());
                String cmd = (String) var;
                if (keyList.contains(cmd)) {
                    Class<?> cls = cmdMan.getResponder(cmd);
                    try {
                        CmdResponder responder = (CmdResponder) cls.getDeclaredConstructor(Serializable.class)
                                .newInstance(new Message());
                        String man = responder.getManual();
                        System.out.printf(
                                "- %s -\n\t%s\n",
                                cmd, man == null ? "(This command doesn't have a manual)" : man
                        );
                        if (man == null) return 0;
                        keyList.remove(cmd);
                        boolean haveSame = false;
                        for (String sub : keyList) {
                            Class<?> clsSub = cmdMan.getResponder(sub);
                            responder = (CmdResponder) clsSub.getDeclaredConstructor(Serializable.class)
                                    .newInstance(new Message());
                            if (Objects.equals(responder.getManual(), man)) {
                                if (!haveSame) {
                                    haveSame = true;
                                    System.out.print("Command(s) with same effect:");
                                }
                                System.out.printf(" '%s'", sub);
                            }
                        }
                        if (haveSame) System.out.println();
                    } catch (NoSuchMethodException | InstantiationException |
                            IllegalAccessException | InvocationTargetException e) {
                        Sys.errF("Help", "Unable to make instance of cmd '%s'.", cmd);
                        Sys.exit(6);
                    }
                } else {
                    Sys.errF("Help", "No such command named '%s'.", cmd);
                }
            }
        } else {
            ArrayList<String> keyList =
                    new ArrayList<>(cmdMan.getKeySet());
            while (!keyList.isEmpty()) {
                if (!outFirst) outFirst = true;
                else System.out.println();
                String cur = keyList.get(0);
                Class<?> cls = cmdMan.getResponder(cur);
                try {
                    CmdResponder responder = (CmdResponder) cls.getDeclaredConstructor(Serializable.class)
                            .newInstance(new Message());
                    System.out.print("- " + cur);
                    keyList.remove(cur);
                    String man = responder.getManual();
                    for (int i = 0; i < keyList.size(); i++) {
                        String sub = keyList.get(i);
                        Class<?> clsSub = cmdMan.getResponder(sub);
                        responder = (CmdResponder) clsSub.getDeclaredConstructor(Serializable.class)
                                .newInstance(new Message());
                        if (Objects.equals(man, responder.getManual())) {
                            System.out.print(" | " + sub);
                            keyList.remove(sub);
                            i = -1;
                        }
                    }
                    if (man == null) man = "(These commands doesn't have a manual)";
                    System.out.println(" -\n\t" + man);
                } catch (NoSuchMethodException | InstantiationException |
                        IllegalAccessException | InvocationTargetException e) {
                    Sys.errF("Help", "Unable to make instance of cmd '%s'.", cur);
                    Sys.exit(5);
                }
            }
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        return 0;
    }

    @Override
    public String getManual() {
        return "If no parameter, this will list all the commands and manual. Else, use 'help [cmd] or man [cmd]' " +
                "to show the cmd manual.";
    }
}
