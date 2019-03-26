package top.chorg.Kernel.Managers;

import top.chorg.System.Sys;

/**
 * Basement responder for FlagManager to expand functions.
 * Method overview:
 *  - Methods must be inherited:
 *      response()              Master response method.
 *      getManual()             To provide this flag's usage manual.
 *                              Will be used when invoking 'help' command.
 *      aftActions()            Defines the operations that you want to execute after the flag analyse.
 *  - Methods can be used in two major response methods:
 *      getArg()                Get an argument from the arg list.
 */
public abstract class FlagResponder {

    /**
     * The master execution method realize the flag's effect.
     *
     * @return The return value of your responder. Return 0 if it's working fine.
     * If not working properly, please return a value ranged 1 ~ 99. Try to void code collisions.
     */
    public abstract int response();

    /**
     * To get this flag's usage manual.
     *
     * @return The usage manual of this flag. Try to avoid using '\n' char to avoid new lines.
     */
    public abstract String getManual();

    /**
     * Defines the operations that you want to execute after the flag analyse.
     */
    public abstract void aftActions();

    /**
     * Get an argument from the arg list. (The next string)
     * An scene of using: if you want user to input password, then let them input: '-p 123456'
     * If you want to process that, you need to use getArg() method to get the string '123456'.
     *
     * @return The argument string.
     */
    protected String getArg() {
        try {
            return FlagManager.provideArg();
        } catch (IllegalArgumentException e) {
            Sys.errF(
                    "Flags",
                    "Too few arguments for flag '%s'\nThe usages are:\n\t%s",
                    FlagManager.getCurFlag(), getManual()
            );
            Sys.exit(100);
        }
        return null;
    }
}
