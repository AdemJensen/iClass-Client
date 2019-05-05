package top.chorg.kernel.cmd.publicResponders.auth;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.api.auth.User;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.support.Date;
import top.chorg.support.DateTime;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Scanner;

public class Users extends CmdResponder {

    public Users(String...args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            String cmd = nextArg();
            CmdResponder resp;
            switch (cmd) {
                case "alter":
                    resp = alterUser();
                    break;
                case "changePass":
                    resp = changePassword();
                    break;
                case "inspect":
                    resp = inspectUser();
                    break;
                case "nick":
                    resp = getNickName();
                    break;
                case "real":
                    resp = getRealName();
                    break;
                case "username":
                    resp = getUserName();
                    break;
                case "online":
                    resp = isUserOnline();
                    break;
                default:
                    Sys.errF("User", "Cmd '%s' is invalid.", cmd);
                    return 1;
            }
            if (resp == null) {
                Sys.err("User", "Something went wrong when executing commands.");
                return 2;
            } else {
                while (resp.isAlive()) {}
            }
        } else {
            Sys.err("User", "User is not online, please login first.");
        }
        return 0;
    }


    private CmdResponder alterUser() throws IndexOutOfBoundsException {
        Scanner sc = CmdLineAdapter.sc;
        Sys.clearLine();
        Sys.cmdLinePrint("Now fetching the user info...");
        Global.setVar("USER_INFO_INTERNAL", true);
        Global.cmdManPrivate.execute("fetchUserInfo", String.valueOf(AuthManager.getUser().getId()));
        while (Global.varExists("USER_INFO_INTERNAL")) { }
        User res = Global.getVar("USER_INFO_CACHE", User.class);
        Global.dropVar("USER_INFO_CACHE");
        if (res == null) {
            Sys.err("Alter User", "Error while fetching user info.");
            return null;
        }
        Sys.clearLine();
        Sys.cmdLinePrintln("Now gathering information, if you want to remain unchanged, don't type anything.");
        Sys.cmdLinePrintF("Please input sex (0 = NULL, 1 = Male, 2 = Female) (Now is %d): ", res.getSex());
        String sex = sc.nextLine();
        Sys.cmdLinePrintF("Please input grade (%d): ", res.getGrade()); String grade = sc.nextLine();
        Sys.cmdLinePrintF("Please input avatar (%d): ", res.getAvatar()); String avatar = sc.nextLine();
        Sys.cmdLinePrintF("Please input Birthday (%s): ", res.getBirthday());
        String birthday = sc.nextLine();
        Sys.cmdLinePrintF("Please input realName (%s): ", res.getRealName()); String realName = sc.nextLine();
        Sys.cmdLinePrintF("Please input nickName (%s): ", res.getNickname()); String nickName = sc.nextLine();
        Sys.cmdLinePrintF("Please input email (%s): ", res.getEmail()); String email = sc.nextLine();
        Sys.cmdLinePrintF("Please input phone (%s): ", res.getPhone()); String phone = sc.nextLine();
        Sys.cmdLinePrint("Data input over, pre-checking...");
        try {
            if (sex.length() == 0) sex = String.valueOf(res.getSex());
            if (grade.length() == 0) grade = String.valueOf(res.getGrade());
            if (avatar.length() == 0) avatar = String.valueOf(res.getAvatar());
            if (birthday.length() == 0) {
                birthday = res.getBirthday() == null ? null : res.getBirthday().toString();
            } else {
                new Date(birthday);
            }
            if (realName.length() == 0) realName = res.getRealName();
            if (nickName.length() == 0) nickName = res.getNickname();
            if (email.length() == 0) email = res.getEmail();
            if (phone.length() == 0) phone = res.getPhone();
        } catch (Exception e) {
            Sys.err("Alter User", e.getMessage());
            return null;
        }
        Sys.clearLine();
        Sys.cmdLinePrint("Data pre-check ok, sending to master server...");
        return Global.cmdManPrivate.execute(
                "alterUser",
                sex, grade, avatar, realName, nickName, email, phone, birthday
        );
    }

    public CmdResponder changePassword() {
        if (argAmount() < 2) {
            Sys.warn("Change Password", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Change Password", "For more help, please type 'help join' for more help.");
            return null;
        }
        return Global.cmdManPrivate.execute("changePassword", nextArg(), nextArg());
    }

    public CmdResponder inspectUser() {
        if (argAmount() < 1) {
            Sys.warn("Inspect User", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Inspect User", "For more help, please type 'help join' for more help.");
            return null;
        }
        String req = nextArg();
        int uu;
        try {
            uu = Integer.parseInt(req);
        } catch (NumberFormatException e) {
            Sys.err("Inspect User", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("fetchUserInfo", String.valueOf(uu));
    }

    public CmdResponder getNickName() {
        if (argAmount() < 1) {
            Sys.warn("Get Nick Name", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Get Nick Name", "For more help, please type 'help join' for more help.");
            return null;
        }
        String[] req = remainArgs();
        int[] request = new int[req.length];
        try {
            for (int i = 0; i < req.length; i++) {
                request[i] = Integer.parseInt(req[i]);
            }
        } catch (NumberFormatException e) {
            Sys.err("Get Nick Name", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("getNickName", Global.gson.toJson(request));
    }

    public CmdResponder getRealName() {
        if (argAmount() < 1) {
            Sys.warn("Get Real Name", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Get Real Name", "For more help, please type 'help join' for more help.");
            return null;
        }
        String[] req = remainArgs();
        int[] request = new int[req.length];
        try {
            for (int i = 0; i < req.length; i++) {
                request[i] = Integer.parseInt(req[i]);
            }
        } catch (NumberFormatException e) {
            Sys.err("Get Real Name", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("getRealName", Global.gson.toJson(request));
    }

    public CmdResponder getUserName() {
        if (argAmount() < 1) {
            Sys.warn("Get User Name", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Get User Name", "For more help, please type 'help join' for more help.");
            return null;
        }
        String[] req = remainArgs();
        int[] request = new int[req.length];
        try {
            for (int i = 0; i < req.length; i++) {
                request[i] = Integer.parseInt(req[i]);
            }
        } catch (NumberFormatException e) {
            Sys.err("Get User Name", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("getUserName", Global.gson.toJson(request));
    }

    public CmdResponder isUserOnline() {
        if (argAmount() < 1) {
            Sys.warn("Judge Online", "Arguments for register are too few. Needed two parameters.");
            Sys.info("Judge Online", "For more help, please type 'help join' for more help.");
            return null;
        }
        String[] req = remainArgs();
        int[] request = new int[req.length];
        try {
            for (int i = 0; i < req.length; i++) {
                request[i] = Integer.parseInt(req[i]);
            }
        } catch (NumberFormatException e) {
            Sys.err("Judge Online", "Invalid argument, Digit required.");
            return null;
        }
        return Global.cmdManPrivate.execute("judgeOnline", Global.gson.toJson(request));
    }

    @Override
    public String getManual() {
        return "Actions relevant to users.";
    }

}
