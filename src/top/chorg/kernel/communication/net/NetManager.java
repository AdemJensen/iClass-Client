package top.chorg.kernel.communication.net;

import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.system.Sys;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class NetManager {
    private static HashMap<String, Class<?>> records = new HashMap<>();

    public static NetResponder execute(Message msg) {
        if (!records.containsKey(msg.getMsgType())) {
            HostManager.onInvalidTransmission("Invalid message.");
            return null;
        }
        Class<?> responderClass = records.get(msg.getMsgType());
        try {
            NetResponder responderObj =
                    (NetResponder) (responderClass.getDeclaredConstructor(String.class).newInstance(msg.getContent()));
            responderObj.start();
            return responderObj;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Sys.errF(
                    "Net",
                    "Invalid responder (%s), unable to make response.",
                    msg.getMsgType()
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
                        "Net",
                        "Net '%s' already exists!",
                        cmd
                );
                Sys.exit(17);
            }
            records.put(cmd, response);
        } else {
            Sys.errF(
                    "Net",
                    "Responder '%s' is not a NetResponder!",
                    response
            );
            Sys.exit(18);
        }
    }

    public static void onConnectionLost(String identifier) {

    }

    public static Set<String> getKeySet() {
        return records.keySet();
    }

    public static Class<?> getResponder(String key) {
        return records.get(key);
    }

}
