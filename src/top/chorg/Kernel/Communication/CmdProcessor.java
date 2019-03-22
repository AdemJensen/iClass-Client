package top.chorg.Kernel.Communication;

import top.chorg.System.Sys;

import java.util.HashMap;

/**
 * Master processor used to process the commands from the client side.
 */
public class CmdProcessor {
    private static HashMap<String, CmdResponder> records = new HashMap<>();

    public static int execute(String cmd) {
        return execute(cmd.split(" "));
    }

    public static int execute(String[] cmd) {
        if (!records.containsKey(cmd[0])) {
            Sys.warnF(
                    "CMD",
                    "Command '%s' not exist! Use 'help' to display all the possible commands.",
                    cmd[0]
            );
            return 0;
        }
        int returnValue = records.get(cmd[0]).response(cmd);
        if (returnValue != 0) {
            Sys.warnF(
                    "CMD",
                    "Command execution returned error code (%d).",
                    returnValue
            );
            Sys.exit(150 + returnValue);
        }
        return returnValue;
    }

    public static void register(String cmd, CmdResponder response) {
        if (records.containsKey(cmd)) {
            Sys.errF(
                    "CMD",
                    "CMD '%s' already exists!",
                    cmd
            );
            Sys.exit(9);
        }
        records.put(cmd, response);
    }

}
