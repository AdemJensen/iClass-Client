package top.chorg.kernel.cmd.privateResponders.auth;

import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.auth.AlterUserRequest;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.support.Date;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Objects;

public class AlterUser extends CmdResponder {
    public AlterUser(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            int sex = Objects.requireNonNull(nextArg(int.class));
            int grade = Objects.requireNonNull(nextArg(int.class));
            int avatar = Objects.requireNonNull(nextArg(int.class));
            String realName = nextArg();
            String nickName = nextArg();
            String email = nextArg();
            String phone = nextArg();
            String date = nextArg();
            if (!Global.masterSender.send(new Message(
                    "alterUser",
                    Global.gson.toJson(new AlterUserRequest(
                            sex,
                            grade,
                            avatar,
                            realName,
                            nickName,
                            email,
                            phone,
                            date == null ? null : new Date(date)
                    ))
            ))) {
                Sys.err("Alter User", "Unable to send request.");
            }
        } else {
            Sys.err("Alter User", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        if (results == null) {
            HostManager.onInvalidTransmission("Alter User: on invalid result.");
            return 1;
        }
        if (results.equals("OK")) {
            Sys.info("Alter User", "Successful operation.");
            if (!AuthManager.updateUserInfo()) {
                Sys.info("Alter User", "Problem occurred while refreshing user.");
                AuthManager.bringOffline();
            }
        } else {
            Sys.errF("Alter User", "Error: %s.", results);
        }
        // TODO: GUI process
        return 0;
    }
}
