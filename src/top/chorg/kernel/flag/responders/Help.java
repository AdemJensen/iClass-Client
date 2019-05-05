package top.chorg.kernel.flag.responders;

import top.chorg.kernel.flag.FlagManager;
import top.chorg.kernel.flag.FlagResponder;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This responder generates the command line help according to the getManual() declared in the Responders.
 * Notice that this responder will group up the flags that with the same manual content.
 * The helper will display all the flags without manual in a group.
 * CAUTION: DO NOT return a void string! If you have nothing to say, please return null in the getManual().
 */
public class Help extends FlagResponder {
    @Override
    public int response() {
        Global.setVar("HELP_MODE", true);
        return 0;
    }

    @Override
    public String getManual() {
        return "Display all the startup flags and its functions. Notice that with this flag, all the other flags" +
                " will be ignored and the system will enter a mode called 'Manual Mode'.";
    }

    @Override
    public void aftActions() {
        if (FlagManager.processedNum() > 0) {
            Sys.warn(
                    "Flag Manual",
                    "The Manual Mode is enabled, the system will only display manuals and stop."
            );
        }
        ArrayList<String> keyList = new ArrayList<>(FlagManager.getKeySet());
        boolean outFirst = false;
        while (!keyList.isEmpty()) {
            if (!outFirst) outFirst = true;
            else System.out.println();
            String cur = keyList.get(0);
            System.out.print(cur);
            FlagResponder responder = FlagManager.getResponder(cur);
            String man = responder.getManual();
            keyList.remove(cur);
            for (int i = 0; i < keyList.size(); i++) {
                String sub = keyList.get(i);
                FlagResponder responderSub = FlagManager.getResponder(sub);
                if (Objects.equals(man, responderSub.getManual())) {
                    System.out.printf(" | %s", sub);
                    keyList.remove(sub);
                    i = -1;
                }
            }
            if (man == null) man = "(These flags doesn't have manual.)";
            System.out.println("\n\t" + man);
        }
        Sys.exit(0);
    }
}
