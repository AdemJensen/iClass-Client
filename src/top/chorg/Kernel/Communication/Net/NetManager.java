package top.chorg.Kernel.Communication.Net;

import top.chorg.Kernel.Communication.Message;
import top.chorg.System.Sys;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class NetManager {
    private static HashMap<String, Class<?>> records = new HashMap<>();

    public static NetResponder execute(Message msg) {
        if (!records.containsKey(msg.msgType)) {
            Sys.warnF(
                    "Net",
                    "Incoming invalid transmission (code %d), ignored.",
                    msg.msgType
            );
            return null;
        }
        Class<?> responderClass = records.get(msg.msgType);
        try {
            NetResponder responderObj =
                    (NetResponder) (responderClass.getDeclaredConstructor(Serializable.class).newInstance(msg.content));
            responderObj.start();
            return responderObj;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Sys.errF(
                    "CMD",
                    "Invalid responder (%s), unable to make response.",
                    msg.msgType
            );
            Sys.exit(16);
        }
        return null;
    }

    public static boolean cmdExists(String key) {
        return records.containsKey(key);
    }

    public static void register(String cmd, Class<?> response) {
        if (response.getSuperclass().equals(NetResponder.class)) {
            if (records.containsKey(cmd)) {
                Sys.errF(
                        "CMD",
                        "CMD '%s' already exists!",
                        cmd
                );
                Sys.exit(17);
            }
            records.put(cmd, response);
        } else {
            Sys.errF(
                    "CMD",
                    "Responder '%s' is not a CmdResponder!",
                    response
            );
            Sys.exit(18);
        }
    }

    public static Set<String> getKeySet() {
        return records.keySet();
    }

    public static Class<?> getResponder(String key) {
        return records.get(key);
    }

}
