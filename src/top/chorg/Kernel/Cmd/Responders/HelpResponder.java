package top.chorg.Kernel.Cmd.Responders;

import top.chorg.Kernel.Cmd.CmdManager;
import top.chorg.Kernel.Cmd.CmdResponder;
import top.chorg.System.Sys;

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
    @Override
    public int response() {
        Object[] vars = (Object[]) args;
        boolean outFirst = false;
        if (vars.length > 0) {
            for (Object var : vars) {
                if (!outFirst) outFirst = true;
                else System.out.println();
                ArrayList<String> keyList = new ArrayList<>(CmdManager.getKeySet());
                String cmd = (String) var;
                if (keyList.contains(cmd)) {
                    Class<?> cls = CmdManager.getResponder(cmd);
                    try {
                        CmdResponder responder = (CmdResponder) cls.getDeclaredConstructor().newInstance();
                        String man = responder.getManual();
                        System.out.printf("- %s -\n\t%s\n", cmd, man == null ? "(This command doesn't have a manual)" : man);
                        if (man == null) return 0;
                        keyList.remove(cmd);
                        boolean haveSame = false;
                        for (String sub : keyList) {
                            Class<?> clsSub = CmdManager.getResponder(sub);
                            CmdResponder responderSub = (CmdResponder) clsSub.getDeclaredConstructor().newInstance();
                            if (Objects.equals(responderSub.getManual(), man)) {
                                if (!haveSame) {
                                    haveSame = true;
                                    System.out.print("Command(s) with same effect:");
                                }
                                System.out.printf(" '%s'", sub);
                            }
                        }
                        if (haveSame) System.out.println();
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        Sys.errF("Help", "Unable to make instance of cmd '%s'.", cmd);
                        Sys.exit(6);
                    }
                } else {
                    Sys.errF("Help", "No such command named '%s'.", cmd);
                }
            }
        } else {
            ArrayList<String> keyList = new ArrayList<>(CmdManager.getKeySet());
            while (!keyList.isEmpty()) {
                if (!outFirst) outFirst = true;
                else System.out.println();
                String cur = keyList.get(0);
                Class<?> cls = CmdManager.getResponder(cur);
                try {
                    CmdResponder responder = (CmdResponder) cls.getDeclaredConstructor().newInstance();
                    System.out.print("- " + cur);
                    keyList.remove(cur);
                    String man = responder.getManual();
                    for (int i = 0; i < keyList.size(); i++) {
                        String sub = keyList.get(i);
                        Class<?> clsSub = CmdManager.getResponder(sub);
                        CmdResponder responderSub = (CmdResponder) clsSub.getDeclaredConstructor().newInstance();
                        if (Objects.equals(man, responderSub.getManual())) {
                            System.out.print(" | " + sub);
                            keyList.remove(sub);
                            i = -1;
                        }
                    }
                    if (man == null) man = "(These commands doesn't have a manual)";
                    System.out.println(" -\n\t" + man);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
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
        return "If no parameter, this will list all the commands. Else, use 'help [cmd] or man [cmd]' " +
                "to show the cmd manual.";
    }
}
