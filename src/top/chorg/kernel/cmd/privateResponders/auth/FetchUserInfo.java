package top.chorg.kernel.cmd.privateResponders.auth;

import com.google.gson.JsonParseException;
import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.auth.User;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

public class FetchUserInfo extends CmdResponder {
    public FetchUserInfo(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "fetchUserInfo",
                    nextArg()       // Only 1 user.
            ))) {
                Sys.err("Fetch User Info", "Unable to send request.");
            }
        } else {
            Sys.err("Fetch User Info", "User is not online, please login first.");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String results = nextArg();
        User info;
        try {
            info = Global.gson.fromJson(results, User.class);
            if (info == null) throw new JsonParseException("e");
        } catch (JsonParseException e) {
            Sys.errF("Fetch User Info", "Error: %s", results);
            Global.dropVar("USER_INFO_INTERNAL");
            return 1;
        }
        if (Global.varExists("USER_INFO_INTERNAL")) {
            Global.setVar("USER_INFO_CACHE", info);
            Global.dropVar("USER_INFO_INTERNAL");
            return 0;
        }
        Sys.clearLine();
        Sys.cmdLinePrintF("Got user %d's info:\n", info.getId());
        Sys.cmdLinePrintF("username: %s\n", info.getUsername());
        if (info.getEmail() != null)
            Sys.cmdLinePrintF("email: %s\n", info.getEmail());
        if (info.getPhone() != null)
            Sys.cmdLinePrintF("phone: %s\n", info.getPhone());
        if (info.getBirthday() != null)
            Sys.cmdLinePrintF("birthday: %s\n", info.getBirthday());
        switch (info.getSex()) {
            case 1:
                Sys.cmdLinePrintF("sex: male\n");
                break;
            case 2:
                Sys.cmdLinePrintF("sex: female\n");
                break;
        }
        if (info.getRealName() != null)
            Sys.cmdLinePrintF("realName: %s\n", info.getRealName());
        if (info.getNickname() != null)
            Sys.cmdLinePrintF("nickName: %s\n", info.getNickname());
        if (info.getGrade() != 0)
            Sys.cmdLinePrintF("grade: %d\n", info.getGrade());
        if (info.getClassId().length > 0) {
            Sys.cmdLinePrint("Joined classes: ");
            boolean isOut = false;
            for (int i : info.getClassId()) {
                if (isOut) Sys.cmdLinePrint(", ");
                isOut = true;
                Sys.cmdLinePrintF("%d", i);
            }
            Sys.cmdLinePrint("\n");
        }
        Sys.cmdLinePrintF("regTime: %s\n", info.getRegTime());
        CmdLineAdapter.outputDecoration();
        // TODO: GUI process
        return 0;
    }
}
