package top.chorg.Kernel.Communication.Net;

import top.chorg.System.Global;

import java.io.Serializable;

/**
 * Basement class, must be inherited.
 */
public abstract class NetResponder extends Thread {
    protected Serializable args;            // Arguments passed through assignArgs(Serializable).
    protected boolean responseMode = true;  // To judge if the run() method should use response() or onReceiveNetMsg().
    private int returnVal = (int) Global.getVar("inProcessReturnValue");
    // Return code after response. If in process, the value will be the same as Global.getVar("inProcessReturnValue")

    /**
     * Assign the arg list for two execution function to provide args.
     * Invoked only by managers.
     * WARNING: MUST BE OVERRIDE IN SUB CLASS!
     *
     * @param args Arguments to be provided.
     */
    public NetResponder(Serializable args) {
        this.args = args;
    }

    /**
     * Master response method (1#).
     * Will be invoked at NetManager when receiving message and assigned.
     *
     * @return return value of this responder action.
     */
    public abstract int response();

    /**
     * Master execution method for Thread standard.
     */
    public final void run() {
        returnVal = this.response();
    }

    /**
     * Get the return value of either execution method.
     * If thread still running, the return value will return Global.getVar("inProcessReturnValue") instead.
     *
     * @return The return value of either execution method.
     */
    public final int getReturnVal() {
        return returnVal;
    }

}
