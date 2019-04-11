package top.chorg.kernel.cmd;

import top.chorg.system.Sys;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

/**
 * Master processor used to process the commands from the client side.
 */
public class CmdManager {
    private HashMap<String, Class<?>> records = new HashMap<>();

    public CmdResponder execute(String...args) {
        if (!records.containsKey(args[0])) {
            Sys.warnF(
                    "CMD",
                    "Command '%s' doesn't exist! Use 'help' to display all the possible commands.",
                    args[0]
            );
            return null;
        }
        Class<?> responderClass = records.get(args[0]);
        try {
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            CmdResponder responderObj =
                    (CmdResponder) (responderClass.getDeclaredConstructor(String[].class).newInstance((Object) subArgs));
            responderObj.start();
            return responderObj;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Sys.errF(
                    "CMD",
                    "Invalid responder (%s), unable to make response.",
                    args[0]
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
