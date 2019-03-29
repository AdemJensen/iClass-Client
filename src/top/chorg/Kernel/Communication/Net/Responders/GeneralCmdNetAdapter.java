package top.chorg.Kernel.Communication.Net.Responders;

import top.chorg.Kernel.Cmd.CmdResponder;
import top.chorg.Kernel.Communication.Auth.AuthManager;
import top.chorg.Kernel.Communication.HostManager;
import top.chorg.Kernel.Communication.Message;
import top.chorg.Kernel.Communication.Net.NetManager;
import top.chorg.Kernel.Communication.Net.NetReceiver;
import top.chorg.Kernel.Communication.Net.NetResponder;
import top.chorg.System.Sys;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class GeneralCmdNetAdapter extends NetResponder {

    private static HashMap<String, Class<?>> records = new HashMap<>();

    public GeneralCmdNetAdapter(Serializable args) {
        super(args);
    }

    @Override
    public int response() {

        return 0;
    }

    public static CmdResponder execute(Message msg) {
        if (!records.containsKey(msg.msgType)) {
            HostManager.onInvalidTransmission("");
            return null;
        }
        Class<?> responderClass = records.get(msg.msgType);
        try {
            CmdResponder responderObj =
                    (CmdResponder) (responderClass.getDeclaredConstructor(Serializable.class).newInstance(msg.content));
            responderObj.setResponseMode(false);
            responderObj.start();
            return responderObj;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Sys.errF(
                    "Net Adapter",
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
        if (response.getSuperclass().equals(CmdResponder.class)) {
            if (records.containsKey(cmd)) {
                Sys.errF(
                        "Net Adapter",
                        "Cmd '%s' already exists!",
                        cmd
                );
                Sys.exit(17);
            }
            records.put(cmd, response);
        } else {
            Sys.errF(
                    "Net Adapter",
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
