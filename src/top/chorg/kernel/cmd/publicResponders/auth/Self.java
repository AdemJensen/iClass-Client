package top.chorg.kernel.cmd.publicResponders.auth;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Sys;

public class Self extends CmdResponder {

    public Self(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            Sys.clearLine();
            Sys.cmdLinePrintF("id: %d\n", AuthManager.getUser().getId());
            Sys.cmdLinePrintF("username: %s\n", AuthManager.getUser().getUsername());
            if (AuthManager.getUser().getEmail() != null)
                Sys.cmdLinePrintF("email: %s\n", AuthManager.getUser().getEmail());
            if (AuthManager.getUser().getPhone() != null)
                Sys.cmdLinePrintF("phone: %s\n", AuthManager.getUser().getPhone());
            if (AuthManager.getUser().getBirthday() != null)
                Sys.cmdLinePrintF("birthday: %s\n", AuthManager.getUser().getBirthday());
            switch (AuthManager.getUser().getSex()) {
                case 1:
                    Sys.cmdLinePrintF("sex: male\n");
                    break;
                case 2:
                    Sys.cmdLinePrintF("sex: female\n");
                    break;
            }
            if (AuthManager.getUser().getRealName() != null)
                Sys.cmdLinePrintF("realName: %s\n", AuthManager.getUser().getRealName());
            if (AuthManager.getUser().getNickname() != null)
                Sys.cmdLinePrintF("nickName: %s\n", AuthManager.getUser().getNickname());
            if (AuthManager.getUser().getGrade() != 0)
                Sys.cmdLinePrintF("grade: %d\n", AuthManager.getUser().getGrade());
            if (AuthManager.getUser().getClassId().length > 0) {
                Sys.cmdLinePrint("Joined classes: ");
                boolean isOut = false;
                for (int i : AuthManager.getUser().getClassId()) {
                    if (isOut) Sys.cmdLinePrint(", ");
                    isOut = true;
                    Sys.cmdLinePrintF("%d", i);
                }
                Sys.cmdLinePrint("\n");
            }
            Sys.cmdLinePrintF("regTime: %s\n", AuthManager.getUser().getRegTime());
            CmdLineAdapter.outputDecoration();
        } else {
            Sys.err("Self", "User is not online, please login first!");
        }
        return 0;
    }

    @Override
    public String getManual() {
        return "Get the current operative user information.";
    }
}
