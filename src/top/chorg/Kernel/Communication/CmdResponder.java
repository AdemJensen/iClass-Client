package top.chorg.Kernel.Communication;

/**
 * Basement class, must be inherited.
 */
public abstract class CmdResponder {

    public abstract int response(String[] args);

    public abstract void onReceiveNetMsg(String msg);

    protected final int sendNetMsg(String msg) {
        return 0;
    }

}
