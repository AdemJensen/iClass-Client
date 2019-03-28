package top.chorg.Kernel.Cmd;

import top.chorg.System.Global;

import java.io.Serializable;

/**
 * Basement responder for CmdManager to expand functions.
 * Method overview:
 *  - Methods must be inherited:
 *      response()              Master response method (1#).
 *      onReceiveNetMsg()       Net response method (2#). If no need, just keep it void and return 0.
 *      getManual()             To provide this command's usage manual.
 *                              Will be used when invoking 'help' command.
 *  - Methods can be used in two major response methods:
 *      sendNetMsg()            To send something to the NetSender and let the message sent to the remote host.
 *  - Variables can be used in two major response methods:
 *      args                    Arguments passed through assignArgs(Serializable).
 */
public abstract class CmdResponder extends Thread {
    protected Serializable args;        // Arguments passed through assignArgs(Serializable).
    protected boolean complete = false; // Used to check if current thread is still running.
    protected boolean responseMode = true; // To judge if the run() method should use response() or onReceiveNetMsg().
    private int returnVal = (int) Global.getVar("inProcessReturnValue");
    // Return code after response. If in process, the value will be the same as Global.getVar("inProcessReturnValue")

    /**
     * Assign the arg list for two execution function to provide args.
     * Invoked only by managers.
     * WARNING: MUST BE OVERRIDE IN SUB CLASS!
     *
     * @param args Arguments to be provided.
     */
    public CmdResponder(Serializable args) {
        this.args = args;
    }

    /**
     * Master response method (1#).
     * Will be invoked at CmdManager when assigned.
     *
     * @return return value of this responder action.
     */
    public abstract int response();

    /**
     * Net response method (2#). If no need, just keep it void and return 0.
     * Will be invoked at CmdNetAdapter when received message and assigned by NetManager.
     *
     * @return return value of this responder action.
     */
    public abstract int onReceiveNetMsg();

    /**
     * To provide this command's usage manual.
     *
     * @return The usage manual of this command. Try to avoid using '\n' char to avoid new lines.
     */
    public abstract String getManual();

    /**
     * To send something to the NetSender and let the message sent to the remote host.
     * Invoked from two execution functions.
     *
     * @param args Information to be sent.
     */
    protected final void sendNetMsg(Serializable args) {
        // TODO: Insert the NetSender methods.
    }

    /**
     * To judge if the thread is done.
     *
     * @return if the thread is over, this method will return true.
     */
    public boolean isDone() {
        return !this.isAlive();
    }

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
        if (responseMode) {
            returnVal = this.response();
        } else {
            returnVal = this.onReceiveNetMsg();
        }
        this.complete = true;
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
