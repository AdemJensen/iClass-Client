package top.chorg;

import top.chorg.System.Global;
import top.chorg.System.Initializer;
import top.chorg.System.Sys;

import static top.chorg.System.Global.glob;

public class Main {

    public static void main(String[] args) {

        Initializer.execute(args);

        System.out.println(Sys.isCmdEnv());
        System.out.println(Sys.isDevEnv());

    }
}
