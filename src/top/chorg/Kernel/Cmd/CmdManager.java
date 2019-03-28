package top.chorg.Kernel.Cmd;

import top.chorg.Kernel.Communication.Message;
import top.chorg.System.Sys;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

/**
 * Master processor used to process the commands from the client side.
 */
public class CmdManager {
    private HashMap<String, Class<?>> records = new HashMap<>();

    public CmdResponder execute(Message msg) {
        if (!records.containsKey(msg.msgType)) {
            Sys.warnF(
                    "CMD",
                    "Command '%s' doesn't exist! Use 'help' to display all the possible commands.",
                    msg.msgType
            );
            return null;
        }
        Class<?> responderClass = records.get(msg.msgType);
        try {
            CmdResponder responderObj =
                    (CmdResponder) (responderClass.getDeclaredConstructor(Serializable.class).newInstance(msg.content));
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

    public boolean cmdExists(String key) {
        return records.containsKey(key);
    }

    public void register(String cmd, Class<?> response) {
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

    public Set<String> getKeySet() {
        return records.keySet();
    }

    public Class<?> getResponder(String key) {
        return records.get(key);
    }

}
