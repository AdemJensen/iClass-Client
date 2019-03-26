package top.chorg.Kernel.Managers;

import java.io.Serializable;

/**
 * Basement class, must be inherited.
 */
public abstract class NetResponder {
    // TODO: Not formed.

    protected Serializable args;
    protected boolean running = false;

    public final void assignArgs(Serializable args) {
        this.args = args;
    }

    public abstract int response();

    public boolean isRunning() {
        return this.running;
    }

    protected final int sendNetMsg(String msg) {
        return 0;
    }

    public final void run() {
        this.running = true;
        this.response();
        this.running = false;
    }

}
