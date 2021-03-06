package top.chorg.kernel.cmd.privateResponders.auth;

import com.google.gson.JsonSyntaxException;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.auth.AuthInfo;
import top.chorg.kernel.communication.api.auth.AuthResult;
import top.chorg.kernel.communication.net.NetReceiver;
import top.chorg.kernel.communication.net.NetSender;
import top.chorg.support.MD5;
import top.chorg.support.Timer;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class Register extends CmdResponder {

    public Register(String...args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (Global.varExists("AUTH_TIMER")) {
            Sys.err("Register", "Ongoing auth-relevant action in progress, please retry later.");
            Global.guiAdapter.makeEvent("regResult", "Ongoing auth-relevant action in progress.");
            return 208;
        }
        Sys.info("Register", "Attempting to register.");
        Global.setVar("AUTH_TIMER", new Timer(10000, (Object[] args) -> {
            Sys.err("Register", "Timed out while sending reg info (207).");
            Global.guiAdapter.makeEvent("regResult", "Timed out while sending reg info (207).");
            dropTimer();
            return 0;
        }));
        if (HostManager.connect(
                "CmdHost",
                Global.conf.Cmd_Server_Host,
                Global.conf.Cmd_Server_Port
        ) != 0) {
            dropTimer();
            return 210;
        }
        Global.masterSender = new NetSender("CmdHost");
        Global.masterReceiver = new NetReceiver("CmdHost");
        Global.masterReceiver.start();
        AuthInfo authInfo = new AuthInfo(nextArg(), MD5.encode(nextArg()), true);
        if (!Global.masterSender.send(new Message("login", Global.gson.toJson(authInfo)))) {
            Sys.err("register", "Unable to send reg info (211).");
            Global.guiAdapter.makeEvent("regResult", "Unable to send reg info (211).");
            dropTimer();
            return 211;
        }
        while (Global.getVar("AUTH_TIMER") != null) { }
        dropTimer();
        return 0;
    }

    public static void dropTimer() {
        if (Global.varExists("AUTH_TIMER")) {
            Global.getVarCon("AUTH_TIMER", Timer.class).stop();
            clearTimer();
        }
    }

    public static void clearTimer() {
        HostManager.disconnect("CmdHost");
        Global.dropVar("AUTH_TIMER");
    }

    @Override
    public int onReceiveNetMsg() {
        AuthResult res;
        try {
            res = nextArg(AuthResult.class);
        } catch(JsonSyntaxException e) {
            HostManager.onInvalidTransmission("Invalid message content (1)");
            return 1;
        }
        try {
            if (res != null) {
                switch (res.result) {
                    case "Granted":
                        Sys.info("Register", "Successfully registered a user.");
                        dropTimer();
                        Global.guiAdapter.makeEvent("regResult", "OK");
                        return 0;
                    case "Denied":
                        Sys.errF("Register", "Register denied : (%s)", res.obj);
                        dropTimer();
                        Global.guiAdapter.makeEvent("regResult", res.obj);
                        return 6;
                    default:
                        HostManager.onInvalidTransmission("Invalid message content (2)");
                        return 2;
                }
            } else {
                HostManager.onInvalidTransmission("Invalid message content (2)");
                return 2;
            }
        } catch (ClassCastException | JsonSyntaxException e) {
            HostManager.onInvalidTransmission("Invalid message content (3)");
            dropTimer();
            return 3;
        } catch (Exception e) {
            HostManager.onInvalidTransmission("Unknown error (254)");
            Global.guiAdapter.makeEvent("regResult", "Unknown error (254)");
            dropTimer();
            return 254;
        }
    }
}
