package top.chorg.kernel.cmd.publicResponders.Announce;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.api.announcements.FetchListResult;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.support.DateTime;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Scanner;

public class Announce extends CmdResponder {

    public Announce(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            String cmd = nextArg();
            CmdResponder resp;
            switch (cmd) {
                case "ls":
                    resp = fetchList();
                    break;
                case "add":
                    resp = addAnnounce();
                    break;
                case "alter":
                    resp = alterAnnounce();
                    break;
                case "del":
                    resp = delAnnounce();
                    break;
                default:
                    Sys.errF("Announce", "Cmd '%s' is invalid.", cmd);
                    return 1;
            }
            if (resp == null) {
                Sys.err("Announce", "Something went wrong when executing commands.");
                return 2;
            }
            while (resp.isAlive()) {}
        } else {
            Sys.err("Announce", "User is not online, please login first.");
        }
        return 0;
    }

    public CmdResponder fetchList() {
        if (hasNextArg()) {
            if (nextArg().equals("published")) {
                return Global.cmdManPrivate.execute(
                        "fetchAnnounceList",
                        "published"
                );
            }
        }
        return Global.cmdManPrivate.execute(
                "fetchAnnounceList",
                "all"
        );
    }

    public CmdResponder addAnnounce() {
        Scanner sc = CmdLineAdapter.sc;
        Sys.clearLine();
        Sys.cmdLinePrint("Please input title: "); String title = sc.nextLine();
        Sys.cmdLinePrint("Please input content: "); String content = sc.nextLine();
        Sys.cmdLinePrint("Please input Exceed time (if always valid, please input 'never'): ");
        String validity = sc.nextLine();
        if (validity.equals("never")) validity = "2000-01-01 00:00:00";
        Sys.cmdLinePrint("Please input class id: "); String classId = sc.nextLine();
        Sys.cmdLinePrint("Please input access level (0 = normal students, 1 = normal managers, " +
                "2 = super admins): ");
        String level = sc.nextLine();
        Sys.cmdLinePrint("Please input current status (0 = valid, 1 = canceled): "); String status = sc.nextLine();
        Sys.cmdLinePrint("Data input over, pre-checking...");
        try {
            if (title.length() == 0) throw new Exception("Announcement must have a title.");
            if (content.length() == 0) throw new Exception("Announcement must have a content.");
            new DateTime(validity);
            Integer.parseInt(classId);
            int lev = Integer.parseInt(level);
            if (lev != 0 && lev != 1 && lev != 2) throw new Exception("Level must be a digit within 0-2.");
            lev = Integer.parseInt(status);
            if (lev != 0 && lev != 1) throw new Exception("Status must be 0 or 1.");
        } catch (Exception e) {
            Sys.err("Add Announce", e.getMessage());
            return null;
        }
        Sys.clearLine();
        Sys.cmdLinePrint("Data pre-check ok, sending to master server...");
        return Global.cmdManPrivate.execute(
                "addAnnounce",
                title, content, validity, classId, level, status
        );
    }

    public CmdResponder alterAnnounce() {
        Scanner sc = CmdLineAdapter.sc;
        Sys.clearLine();
        Sys.cmdLinePrint("Please input announcement id: "); int id = Integer.parseInt(sc.nextLine());
        Sys.cmdLinePrint("Now fetching the announcement...");
        Global.setVar("ANNOUNCE_LIST_INTERNAL", true);
        Global.cmdManPrivate.execute("fetchAnnounceList", "all");
        while (Global.varExists("ANNOUNCE_LIST_INTERNAL")) { }
        FetchListResult[] temp = Global.getVar("ANNOUNCE_LIST_CACHE", FetchListResult[].class);
        Global.dropVar("ANNOUNCE_LIST_CACHE");
        if (temp == null) {
            Sys.err("Alter Announce", "Error while fetching announce.");
            return null;
        }
        FetchListResult res = null;
        for (FetchListResult result : temp) {
            if (result.id == id) {
                res = result;
                break;
            }
        }
        if (res == null) {
            Sys.err("Alter Announce", "Announce id not exist.");
            return null;
        }
        Sys.clearLine();
        Sys.cmdLinePrintln("Now gathering information, if you want to remain unchanged, don't type anything.");
        Sys.cmdLinePrintF("Please input title (%s): ", res.title); String title = sc.nextLine();
        Sys.cmdLinePrintF("Please input content (%s): ", res.content); String content = sc.nextLine();
        boolean isNever = res.validity.smallerThan(res.date);
        Sys.cmdLinePrintF("Please input Exceed time (%s): ", isNever ? "never" : res.validity.toString());
        String validity = sc.nextLine();
        if (validity.equals("never")) validity = "2000-01-01 00:00:00";
        Sys.cmdLinePrintF("Please input class id (%d): ", res.classId); String classId = sc.nextLine();
        Sys.cmdLinePrintF("Please input access level (0 = normal students, 1 = normal managers, " +
                "2 = super admins) (now is %d): ", res.level);
        String level = sc.nextLine();
        Sys.cmdLinePrintF("Please input current status (0 = valid, 1 = canceled) (now is %d): ", res.status);
        String status = sc.nextLine();
        Sys.cmdLinePrint("Data input over, pre-checking...");
        try {
            if (title.length() == 0) title = res.title;
            if (content.length() == 0) content = res.content;
            if (validity.length() == 0) {
                validity = res.validity.toString();
            } else {
                new DateTime(validity);
            }
            if (classId.length() == 0) classId = String.valueOf(res.classId);
            else Integer.parseInt(classId);
            if (level.length() == 0) level = String.valueOf(res.level);
            else {
                int lev = Integer.parseInt(level);
                if (lev != 0 && lev != 1 && lev != 2) throw new Exception("Level must be a digit within 0-2.");
            }
            if (status.length() == 0) status = String.valueOf(res.status);
            else {
                int lev = Integer.parseInt(status);
                if (lev != 0 && lev != 1) throw new Exception("Status must be 0 or 1.");
            }
        } catch (Exception e) {
            Sys.err("Alter Announce", e.getMessage());
            return null;
        }
        Sys.clearLine();
        Sys.cmdLinePrint("Data pre-check ok, sending to master server...");
        return Global.cmdManPrivate.execute(
                "alterAnnounce",
                String.valueOf(id), title, content, validity, classId, level, status
        );
    }

    public CmdResponder delAnnounce() {
        if (hasNextArg()) {
            try {
                String arg = nextArg();
                Integer.parseInt(arg);
                return Global.cmdManPrivate.execute(
                        "delAnnounce",
                        arg
                );
            } catch (Exception e) {
                Sys.err("Del Announce", "Something is wrong with announcement id.");
                return null;
            }
        } else {
            Sys.err("Del Announce", "Announcement id is required.");
        }
        return null;
    }

    @Override
    public String getManual() {
        return "";
    }

}
