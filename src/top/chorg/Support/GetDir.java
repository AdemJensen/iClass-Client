package top.chorg.Support;

import top.chorg.System.Sys;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GetDir {

    /**
     * Get the file list in the given dir with recursion.
     *
     * @param path Relative or absolute path of the dir.
     * @return An array contains the names of the dir.
     */
    public static String[] recursionGetDir(String path) {
        ArrayList<String> fileList = new ArrayList<>();
        getDir(fileList, path, ".*", true);
        return convertArrayList(fileList);
    }

    /**
     * Get the file list in the given dir without recursion.
     *
     * @param path Relative or absolute path of the dir.
     * @return An array contains the names of the dir.
     */
    public static String[] getDir(String path) {
        ArrayList<String> fileList = new ArrayList<>();
        getDir(fileList, path, ".*", false);
        return convertArrayList(fileList);
    }

    /**
     * Get the file list in the given dir with recursion and pregnant pattern filter.
     *
     * @param path Relative or absolute path of the dir.
     * @param pattern Pregnant pattern of the filename.
     * @return An array contains the names of the dir.
     */
    public static String[] recursionGetDir(String path, String pattern) {
        ArrayList<String> fileList = new ArrayList<>();
        getDir(fileList, path, pattern, true);
        return convertArrayList(fileList);
    }

    /**
     * Get the file list in the given dir with pregnant pattern filter.
     *
     * @param path Relative or absolute path of the dir.
     * @param pattern Pregnant pattern of the filename.
     * @return An array contains the names of the dir.
     */
    public static String[] getDir(String path, String pattern) {
        ArrayList<String> fileList = new ArrayList<>();
        getDir(fileList, path, pattern, false);
        return convertArrayList(fileList);
    }

    /**
     * To convert an String ArrayList to an String array.
     *
     * @param list Targeted ArrayList.
     * @return Result String array.
     */
    private static String[] convertArrayList(ArrayList<String> list) {
        String[] result = new String[list.size()];
        list.toArray(result);
        return result;
    }

    /**
     * Private version of the getDir method.
     *
     * @param fileList An ArrayList to store the result.
     * @param path Relative or absolute path of the dir.
     * @param pattern Pregnant pattern of the filename.
     * @param recursion To enable the recursion mode or not.
     */
    private static void getDir(ArrayList<String> fileList, String path, String pattern, boolean recursion) {
        File[] allFiles = new File(path).listFiles();
        if (allFiles == null) {
            Sys.errF(
                    "File",
                    "Cannot read dir tree '%s'.",
                    path
            );
            Sys.exit(8);
        } else {
            for (File file : allFiles) {
                if (file.isFile()) {
                    if (Pattern.matches(pattern, file.getName())) {
                        fileList.add(file.getName());
                    }
                } else if (recursion) {
                    getDir(fileList, file.getAbsolutePath(), pattern, true);
                }
            }
        }
    }
}
