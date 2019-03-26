package top.chorg.Kernel.Managers;

import top.chorg.Kernel.Communication.Message;
import top.chorg.System.Sys;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

/**
 * Master processor used to process the commands from the client side.
 */
public class CmdManager {
    private static HashMap<String, Class<?>> records = new HashMap<>();

    public static CmdResponder execute(Message msg) {
        if (!records.containsKey(msg.msgType)) {
            Sys.warnF(
                    "CMD",
                    "Command '%s' not exist! Use 'help' to display all the possible commands.",
                    msg.msgType
            );
            return null;
        }
        Class<?> responderClass = records.get(msg.msgType);
        try {
            CmdResponder responderObj = (CmdResponder) responderClass.getDeclaredConstructor().newInstance();
            responderObj.assignArgs(msg.content);
            responderObj.start();
            return responderObj;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Sys.errF(
                    "CMD",
                    "Invalid responder (%s), unable to make response.",
                    msg.msgType
            );
            Sys.exit(13);
        }
        return null;
    }

    public static boolean cmdExists(String key) {
        return records.containsKey(key);
    }

    public static void register(String cmd, Class<?> response) {
        if (response.getSuperclass().equals(CmdResponder.class)) {
            if (records.containsKey(cmd)) {
                Sys.errF(
                        "CMD",
                        "CMD '%s' already exists!",
                        cmd
                );
                Sys.exit(3);
            }
            records.put(cmd, response);
        } else {
            Sys.errF(
                    "CMD",
                    "Responder '%s' is not a CmdResponder!",
                    response
            );
            Sys.exit(4);
        }
    }

    public static Set<String> getKeySet() {
        return records.keySet();
    }

    public static Class<?> getResponder(String key) {
        return records.get(key);
    }

}
