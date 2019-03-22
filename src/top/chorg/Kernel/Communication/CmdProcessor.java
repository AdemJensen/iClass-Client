package top.chorg.Kernel.Communication;

public class CmdProcessor {
    private static int maxComThread = 5;
    private static boolean[] comThreadUsed;



    public CmdProcessor() {
        comThreadUsed = new boolean[maxComThread];
    }

}
