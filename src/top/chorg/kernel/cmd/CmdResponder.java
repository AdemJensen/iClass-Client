package top.chorg.kernel.cmd;

import top.chorg.system.Global;
import top.chorg.system.Sys;

/**
 * Basement responder for CmdManager to expand functions.
 * Method overview:
 *  - Constructors must be override:
 *      CmdResponder(Serializable)  Assign the arg list for two execution function to provide args.
 *                                  You can also make some pre-dos to the arg variable.
 *  - Methods must be inherited:
 *      response()                  Master response method (1#).
 *      onReceiveNetMsg()           Net response method (2#). If no need, just keep it void and return 0.
 *      getManual()                 To provide this command's usage manual.
 *                                  Will be used when invoking 'help' command.
 *  - Variables can be used in two major response methods:
 *      args                        Arguments passed through assignArgs(Serializable).
 */
public abstract class CmdResponder extends Thread {
    private String[] args;        // Arguments passed through assignArgs(Serializable).
    private int argIndex = 0;
    private boolean responseMode = true; // To judge if the run() method should use response() or onReceiveNetMsg().
    private int returnVal = Global.getVarCon("PROCESS_RETURN", int.class);
    // Return code after response. If in process, the value will be the same as Global.getVar("PROCESS_RETURN")

    /**
     * Assign the arg list for two execution function to provide args.
     * Invoked only by managers.
     * WARNING: MUST BE OVERRIDE IN SUB CLASS!
     *
     * @param args Arguments to be provided.
     */
    public CmdResponder(String[] args) {
        this.args = args;
    }

    protected final int argAmount() {
        return args.length;
    }

    protected final boolean hasNextArg() {
        return argIndex < args.length;
    }

    protected final String nextArg() throws IndexOutOfBoundsException {
        if (argIndex >= args.length) throw new IndexOutOfBoundsException();
        return args[argIndex++];
    }

    protected final <T> T nextArg(Class<T> classOfT) {
        if (argIndex >= args.length) return null;
        return Global.gson.fromJson(args[argIndex++], classOfT);
    }

    protected final String[] remainArgs() {
        if (argIndex >= args.length) throw new IndexOutOfBoundsException();
        String[] temp = new String[args.length - argIndex];
        System.arraycopy(args, argIndex, temp, 0, args.length - argIndex);
        return temp;
    }

    /**
     * Master response method (1#).
     * Will be invoked at CmdManager when assigned.
     *
     * @return return value of this responder action.
     */
    public abstract int response() throws IndexOutOfBoundsException;

    /**
     * Net response method (2#). If no need, just keep it void and return 0.
     * Will be invoked at CmdNetAdapter when received message and assigned by NetManager.
     *
     * @return return value of this responder action.
     */
    public int onReceiveNetMsg() { return -1; };

    /**
     * To provide this command's usage manual.
     *
     * @return The usage manual of this command. Try to avoid using '\n' char to avoid new lines.
     */
    public String getManual() { return null; };

    /**
     * Assign execution mode to decide which execution method to use.
     *
     * @param responseMode If true, the run() method will use response() method to response.
     *                     If not, the run() method will use onReceiveNetMsg() method to response.
     */
    public final void setResponseMode(boolean responseMode) {
        this.responseMode = responseMode;
    }

    /**
     * Master execution method for Thread standard.
     */
    public final void run() {
        try {
            if (responseMode) {
                returnVal = this.response();
            } else {
                returnVal = this.onReceiveNetMsg();
            }
        } catch (IndexOutOfBoundsException e) {
            Sys.err("Cmd Res", "Not enough argument for cmd to execute.");
        }
    }

    /**
     * Get the return value of either execution method.
     * If thread still running, the return value will return Global.getVar("PROCESS_RETURN") instead.
     *
     * @return The return value of either execution method.
     */
    public final int getReturnVal() {
        return returnVal;
    }

}
