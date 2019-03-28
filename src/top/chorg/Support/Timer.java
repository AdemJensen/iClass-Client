package top.chorg.Support;

/**
 * To set up a timer and invoke the lambda method later.
 * Usage:
 * Timer timer = new Timer([time], (Object[] args) -> {
 *     // Do something
 * }, [arg1], [arg2], ...);
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
    private class TimerThread extends Thread {

        public int returnValue = -1;
        @Override
        public void run() {
            try {
                sleep(time);
            } catch (InterruptedException e) {
                returnValue = -2;
            }
            returnValue = func.execution(args);
        }
    }
    int returnVal;
    public Timer(int millis, TimerInterface func, Object...args) {
        this.time = millis;
        this.func = func;
        this.args = args;
        thread = new TimerThread();
        thread.start();
        while (thread.isAlive());
        returnVal = thread.returnValue;
    }

    public int getResult() {
        return returnVal;
    }

    public void stop() {
        if (thread.isAlive()) thread.interrupt();
    }

}
