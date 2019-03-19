package top.chorg;

import top.chorg.System.Global;
import top.chorg.System.Sys;

import static top.chorg.System.Global.glob;

public class Main {

    public static void main(String[] args) {
        //glob.setConfig("DEV_MODE", true);
        //sys.saveConfig();
        Sys.reloadConfig();
        System.out.println(glob.getConfig("DEV_MODE"));
    }
}
