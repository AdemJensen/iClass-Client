package top.chorg.support;

/**
 * To set up a timer and invoke the lambda method later.
 * Usage:
 * {@code Timer timer = new Timer([time], (Object[] args) -> {
 *     // Do something
 * }, [arg1], [arg2], ...);}
 */
public class Timer {

    /**
     * Basement class for method to pass in.
     */
    public interface TimerInterface {
        int execution(Object[] args);
    }

    private int time;
    private Object[] args;
    private TimerInterface func;
    private TimerThread thread;
    private boolean isStopped = false;
    private class TimerThread extends Thread {

        public int returnValue = 0;
        @Override
        public void run() {
            try {
                sleep(time);
            } catch (InterruptedException e) {
                returnValue = 0;
            }
            if (isStopped) returnValue = 0;
            else returnValue = func.execution(args);
        }
    }
    int returnVal;
    public Timer(int millis, TimerInterface func, Object...args) {
        this.time = millis;
        this.func = func;
        this.args = args;
        thread = new TimerThread();
        thread.start();
        returnVal = thread.returnValue;
    }

    public int getResult() {
        return returnVal;
    }

    public void stop() {
        isStopped = true;
        if (thread.isAlive()) thread.interrupt();
    }

    public void restart() {
        stop();
        isStopped = false;
        thread = new TimerThread();
        thread.start();
        returnVal = thread.returnValue;
    }

}
