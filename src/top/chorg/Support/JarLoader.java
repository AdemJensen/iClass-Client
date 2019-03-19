package top.chorg.Support;

import top.chorg.System.Sys;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 Extracted from
 @link https://blog.csdn.net/jianggujin/article/details/80536019
 */

public class JarLoader extends URLClassLoader {
    private static boolean canCloseJar = false;
    private List<JarURLConnection> cachedJarFiles;

    static {
        // 1.7之后可以直接调用close方法关闭打开的jar，需要判断当前运行的环境是否支持close方法，如果不支持，需要缓存，避免卸载模块后无法删除jar
        try {
            URLClassLoader.class.getMethod("close");
            canCloseJar = true;
        } catch (NoSuchMethodException e) {
            Sys.err("Jar Loader", "No such method as " + e.toString());
        } catch (SecurityException e) {
            Sys.err("Jar Loader", "Security issue on " + e.toString());
        }
    }

    public JarLoader(URL[] urls, ClassLoader parent) {
        super(new URL[] {}, parent);
        init(urls);
    }

    public JarLoader(URL[] urls) {
        super(new URL[] {});
        init(urls);
    }

    public JarLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(new URL[] {}, parent, factory);
        init(urls);
    }

    private void init(URL[] urls) {
        cachedJarFiles = canCloseJar ? null : new ArrayList<>();
        if (urls != null) {
            for (URL url : urls) {
                this.addURL(url);
            }
        }
    }

    @Override
    protected void addURL(URL url) {
        if (!canCloseJar) {
            try {
                // 打开并缓存文件url连接
                URLConnection uc = url.openConnection();
                if (uc instanceof JarURLConnection) {
                    uc.setUseCaches(true);
                    ((JarURLConnection) uc).getManifest();
                    cachedJarFiles.add((JarURLConnection) uc);
                }
            } catch (Exception e) {
                Sys.err("Jar Loader", "Caught exception " + e.toString());
            }
        }
        super.addURL(url);
    }

    public void close() throws IOException {
        if (canCloseJar) {
            try {
                super.close();
            } catch (IOException ioe) {
                Sys.err("Jar Loader", "Caught IO Exception on " + ioe.toString());
            }
        } else {
            for (JarURLConnection conn : cachedJarFiles) {
                conn.getJarFile().close();
            }
            cachedJarFiles.clear();
        }
    }
}