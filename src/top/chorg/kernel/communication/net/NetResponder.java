package top.chorg.kernel.communication.net;

import top.chorg.system.Global;

/**
 * Basement class, must be inherited.
 */
public abstract class NetResponder extends Thread {
    private String obj;            // Arguments passed by NetManager.
    private int returnVal = (int) Global.getVar("PROCESS_RETURN");
    // Return code after response. If in process, the value will be the same as Global.getVar("PROCESS_RETURN")

    /**
     * Assign the arg list for two execution function to provide args.
     * Invoked only by managers.
     * WARNING: MUST BE OVERRIDE IN SUB CLASS!
     *
     * @param obj Arguments to be provided.
     */
    public NetResponder(String obj) {
        this.obj = obj;
    }

    /**
     * Master response method (1#).
     * Will be invoked at NetManager when receiving message and assigned.
     *
     * @return return value of this responder action.
     */
    public abstract int response();

    protected final String getArg() {
        return obj;
    }

    protected final <T> T getArg(Class<T> classOfT) {
        return Global.gson.fromJson(obj, classOfT);
    }

    /**
     * Master execution method for Thread standard.
     */
    public final void run() {
        returnVal = this.response();
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
