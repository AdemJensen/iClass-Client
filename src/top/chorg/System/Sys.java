package top.chorg.System;

import java.util.Formatter;

/**
 * Contains all the system utilities.
 * Fully static, no need to make instance.
 */
public class Sys {

    /**
     * Send master warning message to the console or log.
     * If under dev environment or cmd line environment, the message will be sent to console.
     * Under Normal GUI mode, this message will be transferred into log file.
     *
     * @param sender Message sender name.
     * @param msg Message content.
     */
    public static void warn(String sender, String msg) {
        String content = String.format("[%s] Warning: %s", sender, msg);
        if (isDevEnv() || isCmdEnv()) {
            System.out.println(content);
        } else {
            log(content);
        }
    }

    /**
     * Send master warning message with format to the console or log.
     * If under dev environment or cmd line environment, the message will be sent to console.
     * Under Normal GUI mode, this message will be transferred into log file.
     *
     * @param sender Message sender name.
     * @param format Message content format.
     * @param args Parameters to be filled into format blanks.
     */
    public static void warnF(String sender, String format, Object ... args) {
        warn(sender, new Formatter().format(format, args).toString());
    }

    /**
     * Send master error message to the console or log.
     * If under dev environment or cmd line environment, the message will be sent to console.
     * Under Normal GUI mode, this message will be transferred into log file.
     *
     * @param sender Message sender name.
     * @param msg Message content.
     */
    public static void err(String sender, String msg) {
        String content = String.format("[%s] Error: %s", sender, msg);
        if (isDevEnv() || isCmdEnv()) {
            System.out.println(content);
        } else {
            log(content);
        }
    }

    /**
     * Send master error message with format to the console or log.
     * If under dev environment or cmd line environment, the message will be sent to console.
     * Under Normal GUI mode, this message will be transferred into log file.
     *
     * @param sender Message sender name.
     * @param format Message content format.
     * @param args Parameters to be filled into format blanks.
     */
    public static void errF(String sender, String format, Object ... args) {
        err(sender, new Formatter().format(format, args).toString());
    }

    /**
     * Send information to the console or log.
     * If under dev environment or cmd line environment, the message will be sent to console.
     * Under Normal GUI mode, this message will be transferred into log file.
     *
     * @param sender Message sender name.
     * @param msg Message content.
     */
    public static void info(String sender, String msg) {
        String content = String.format("[%s] Info: %s", sender, msg);
        if (isDevEnv() || isCmdEnv()) {
            System.out.println(content);
        }
    }

    /**
     * Send information with format to the console or log.
     * If under dev environment or cmd line environment, the message will be sent to console.
     * Under Normal GUI mode, this message will be transferred into log file.
     *
     * @param sender Message sender name.
     * @param format Message content format.
     * @param args Parameters to be filled into format blanks.
     */
    public static void infoF(String sender, String format, Object ... args) {
        info(sender, new Formatter().format(format, args).toString());
    }


    /**
     * Send development information output message to the console or log.
     * If under dev environment or cmd line environment, the message will be sent to console.
     * Under Normal GUI mode, this message will be transferred into log file.
     *
     * @param sender Message sender name.
     * @param msg Message content.
     */
    public static void devInfo(String sender, String msg) {
        String content = String.format("[%s] DEV: %s", sender, msg);
        if (isDevEnv() || isCmdEnv()) {
            System.out.println(content);
        }
    }

    /**
     * Send development information output message with format to the console or log.
     * If under dev environment or cmd line environment, the message will be sent to console.
     * Under Normal GUI mode, this message will be transferred into log file.
     *
     * @param sender Message sender name.
     * @param format Message content format.
     * @param args Parameters to be filled into format blanks.
     */
    public static void devInfoF(String sender, String format, Object ... args) {
        devInfo(sender, new Formatter().format(format, args).toString());
    }

    /**
     * To judge if current environment is development env or not.
     * Default value is false.
     *
     * @return True if current environment is development env.
     */
    public static boolean isDevEnv() {
        return Global.varExists("DEV_MODE") && ((boolean) Global.getVar("DEV_MODE"));
    }

    /**
     * To judge if current environment is Command Line env or not.
     * Default value is false.
     *
     * @return True if current environment is Command Line env.
     */
    public static boolean isCmdEnv() {
        if (Global.varExists("GUI_MODE")) return !((boolean) Global.getVar("GUI_MODE"));
        else return false;
    }

    /**
     * To judge if current environment is GUI env or not.
     * Default value is true.
     *
     * @return True if current environment is GUI env.
     */
    public static boolean isGuiEnv() {
        if (Global.varExists("GUI_MODE")) return ((boolean) Global.getVar("GUI_MODE"));
        else return true;
    }

    /**
     * Exit the system with a return value.
     * If in Command Line mode, this method will exit the program directly with a value.
     * If in GUI mode, a window will be popped up to indicate problems.
     *
     * @param returnValue The exit value.
     */
    public static void exit(int returnValue) {
        if (isCmdEnv()) {
            System.exit(returnValue);
        } else {
            // TODO: Add exit window display method.
            System.out.printf("EXIT ERROR!(%d)\n", returnValue);
        }
    }

    /**
     * Make a log and write them to the log file.
     * Logs will be written into the file of log date.
     *
     * @param content Message content.
     */
    public static void log(String content) {
        // TODO: Log system.
        System.out.printf("LOG INCOMPLETE!(%s)\n", content);
    }

    /**
     * Make a log and write them to the log file.
     * Logs will be written into the file of log date.
     *
     * @param sender Message sender name.
     * @param msg Message content.
     */
    public static void log(String sender, String msg) {
        log(String.format("[%s] Log: %s", sender, msg));
    }

    /**
     * Make a log and write them to the log file.
     * Logs will be written into the file of log date.
     *
     * @param sender Message sender name.
     * @param format Message content format.
     * @param args Parameters to be filled into format blanks.
     */
    public static void logF(String sender, String format, Object ... args) {
        log(sender, new Formatter().format(format, args).toString());
    }

}
