package top.chorg.support;

public class StringArrays {
    public static String[] assemble(String a, String[] b) {
        String[] subArg = new String[b.length + 1];
        subArg[0] = a;
        System.arraycopy(b, 0, subArg, 1, b.length);
        return subArg;
    }

    public static String[] assemble(String[] a, String[] b) {
        String[] subArg = new String[a.length + b.length];
        System.arraycopy(a, 0, subArg, 0, a.length);
        System.arraycopy(b, 0, subArg, a.length, b.length);
        return subArg;
    }

    public static String[] assemble(String[] a, String b) {
        String[] subArg = new String[a.length + 1];
        System.arraycopy(a, 0, subArg, 0, a.length);
        subArg[a.length] = b;
        return subArg;
    }
}
