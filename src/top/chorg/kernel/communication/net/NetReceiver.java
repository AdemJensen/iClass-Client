package top.chorg.kernel.communication.net;

import com.google.gson.JsonSyntaxException;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * To process the messages coming from server side.
 */
public class NetReceiver extends Thread {
    String identifier;
    BufferedReader bufferedReader;

    public NetReceiver(String identifier) {
        this.identifier = identifier;
        this.bufferedReader = HostManager.getBufferedReader(identifier);
    }

    public void run() {
        String msg;
        try {
            while (true) {
                msg = bufferedReader.readLine();
                //System.out.println(msg);
                if (msg == null) {
                    HostManager.disconnect(identifier);
                    Sys.warnF(
                            "Net",
                            "Host (%s) connection lost.",
                            identifier
                    );
                    break;
                }
                try {
                    Message decMsg = Global.gson.fromJson(msg, Message.class);
                    if (decMsg.getMsgType().charAt(0) == 'R' && decMsg.getMsgType().charAt(1) == '-') {
                        String cmdName = decMsg.getMsgType().substring(2);
                        if (!Global.cmdManPrivate.cmdExists(cmdName)) {
                            throw new IllegalAccessException();
                        }
                        CmdResponder responderObj = (CmdResponder) Global.cmdManPrivate.getResponder(cmdName)
                                .getDeclaredConstructor(String[].class)
                                .newInstance((Object) new String[]{decMsg.getContent()});
                        responderObj.setResponseMode(false);
                        responderObj.start();
                    } else NetManager.execute(decMsg);
                } catch (JsonSyntaxException | NoSuchMethodException | IllegalAccessException
                        | InstantiationException | InvocationTargetException e) {
                    Sys.devInfoF(
                            "Net",
                            "The connection (%s) received invalid message (%s).",
                            identifier, msg
                    );
                }
            }
        } catch (IOException ignored) { }
    }
}
