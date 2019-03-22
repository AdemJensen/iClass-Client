package top.chorg.Support;

import top.chorg.System.Sys;

import java.util.HashMap;

public class FlagProcessor {
    // Responder storage, contains all the flags and their process methods.
    private static HashMap<String, FlagResponder> records = new HashMap<>();

    private static String[] flags;          // The args passed that passed into the Main.main() method.
    private static String curFlag = "";     // The flag that is being processed.
    private static int curNum = 0;          // Number of flag that are already processed, works as a loop counter.

    /**
     * Master execution method.
     * @param flags The args passed that passed into the Main.main() method.
     */
    public static void execute(String[] flags) {
        FlagProcessor.flags = flags;
        for (curNum = 0; curNum < flags.length; curNum++) {
            if (!isValidFlag(flags[curNum])) {
                Sys.warnF("Flags", "Invalid flag (%d: '%s').", curNum + 1, flags[curNum]);
                continue;
            }
            curFlag = flags[curNum];
            int returnValue = records.get(curFlag).execute();
            if (returnValue != 0) {
                Sys.errF(
                    "Flags",
                    "Invalid flag (%d: '%s').",
                    curNum + 1, flags[curNum]
                );
                Sys.exit(100 + returnValue);
            }
        }
    }

    /**
     * To register a flag into the processor so that it can process a new flag.
     * @param flag The flag that needs to be processed. such as '-GUI', '--DEV-MODE'
     * @param response An object of a FlagResponder inherited class to indicate the actions of this flag.
     */
    public static void register(String flag, FlagResponder response) {
        if (records.containsKey(flag)) {
            Sys.errF(
                "Flags",
                "Flag '%s' already exists!",
                flag
            );
            Sys.exit(7);
        }
        records.put(flag, response);
    }

    /**
     * To tell whether a string is flag or not.
     * Identified with whether the first char of string is '-' or not.
     * @param flag Targeted string to be checked.
     * @return Whether the string is a flag or not.
     */
    private static boolean isFlag(String flag) {
        return flag.charAt(0) == '-';
    }

    /**
     * To tell whether a flag is valid or not.
     * Arguments for the flag will also be identified as invalid flag.
     * @param flag Targeted flag to be checked.
     * @return Whether the flag is valid or not.
     */
    private static boolean isValidFlag(String flag) {
        return isFlag(flag) && records.containsKey(flag);
    }

    /**
     * Used in the FlagResponder basement class only! Do not use this at other places!
     * @return The wanted argument followed after flag.
     * @throws IllegalArgumentException Thrown when too few args were provided for the flag.
     */
    protected static String provideArg() throws IllegalArgumentException {
        if (isFlag(flags[curNum])) {
            throw new IllegalArgumentException(curFlag);
        }
        return flags[curNum++];
    }

    public static void displayAllManual() {

    }
}
