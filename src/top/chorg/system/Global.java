package top.chorg.system;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import top.chorg.gui.GuiAdapter;
import top.chorg.kernel.cmd.CmdManager;
import top.chorg.kernel.communication.net.NetReceiver;
import top.chorg.kernel.communication.net.NetSender;

import java.util.HashMap;

/**
 * Contains all the global variables and configuration variables.
 *
 * <p>Fully static, no need to make instance.
 *
 * <p><strong>Attention: Method {@code reloadConfig()} or {@code assignConfig(SerializableMap)} must be run at lease
 * once to enable the configuration system.</strong>
 *
 * <ul>
 * <li>If you want to read configurations from the conf file, just use {@code reloadConfig()}.</li>
 * <li>If you want to make a new config variable container, use {@code assignConfig(new SerializableMap())}.</li>
 * </ul>
 */
// TODO: Comment error.
public class Global {
    public static CmdManager cmdManPublic = new CmdManager();
    public static CmdManager cmdManPrivate = new CmdManager();

    public static NetSender masterSender;
    public static NetReceiver masterReceiver;

    public static Gson gson = new Gson();    // Json utils.
    public static Config conf = new Config();     // Contains configuration variables.

    public static GuiAdapter guiAdapter;

    private static HashMap<Object, Object> variables = new HashMap<>();     // Contains global variables.

    /**
     * Modify or add a value in the global variable field.
     *
     * @param key Key of targeted global variable.
     * @param value Value of targeted global variable.
     */
    public static void setVar(Object key, Object value) {
        if (variables.containsKey(key)) {
            //String ori = variables.get(key).toString();
            variables.replace(key, value);
            //Sys.devInfoF("Global","Global '%s' replaced ('%s' -> '%s').", key, ori, value.toString());
        } else {
            variables.put(key, value);
        }
    }

    /**
     * Get variable from global variable field, if null will cause system failure.
     *
     * @param key Key of targeted global variable.
     * @return Value of targeted global variable.
     */
    public static <T> T getVarCon(Object key, Class<T> typeOfVal) {
        if (!variables.containsKey(key)) throw new NullPointerException();
        T result = Primitives.wrap(typeOfVal).cast(variables.get(key));
        if (result == null) throw new NullPointerException();
        return result;
    }

    /**
     * Get variable from global variable field.
     *
     * @param key Key of targeted global variable.
     * @return Value of targeted global variable.
     */
    public static <T> T getVar(Object key, Class<T> typeOfVal) {
        if (!variables.containsKey(key)) {
            return null;
        }
        return Primitives.wrap(typeOfVal).cast(variables.get(key));
    }

    /**
     * Get variable from global variable field.
     *
     * @param key Key of targeted global variable.
     * @return Value of targeted global variable.
     */
    public static Object getVar(Object key) {
        if (!variables.containsKey(key)) {
            return null;
        }
        return variables.get(key);
    }

    /**
     * To tell if a global variable exists in the global variable field.
     *
     * @param key Key of targeted global variable.
     * @return True if the global variable exists in the global variable field, false if not.
     */
    public static boolean varExists(Object key) {
        return variables.containsKey(key);
    }

    /**
     * Delete a global variable.
     *
     * @param key Key of targeted global variable.
     */
    public static void dropVar(Object key) {
        if (varExists(key)) variables.remove(key);
    }

}
