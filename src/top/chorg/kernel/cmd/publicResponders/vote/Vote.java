package top.chorg.kernel.cmd.publicResponders.vote;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.api.vote.FetchInfoResult;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.support.DateTime;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Vote extends CmdResponder {

    public Vote(String... args) {
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
                case "get":
                    resp = fetchInfo();
                    break;
                case "make":
                    resp = makeVote();
                    break;
                case "result":
                    resp = getResult();
                    break;
                case "add":
                    resp = addVote();
                    break;
                case "alter":
                    resp = alterVote();
                    break;
                case "del":
                    resp = delVote();
                    break;
                default:
                    Sys.errF("Vote", "Cmd '%s' is invalid.", cmd);
                    return 1;
            }
            if (resp == null) {
                Sys.err("Vote", "Something went wrong when executing commands.");
                return 2;
            } else {
                while (resp.isAlive()) {}
            }
        } else {
            Sys.err("Vote", "User is not online, please login first.");
        }
        return 0;
    }

    private CmdResponder fetchList() throws IndexOutOfBoundsException {
        if (hasNextArg()) {
            if (nextArg().equals("published")) {
                return Global.cmdManPrivate.execute(
                        "fetchVoteList",
                        "published"
                );
            }
        }
        return Global.cmdManPrivate.execute(
                "fetchVoteList",
                "all"
        );
    }

    private CmdResponder fetchInfo() throws IndexOutOfBoundsException {
        return Global.cmdManPrivate.execute(
                "fetchVoteInfo",
                nextArg()
        );
    }

    private CmdResponder makeVote() throws IndexOutOfBoundsException {
        try {
            String voteId = nextArg();
            String addition = "";
            ArrayList<Integer> arr = new ArrayList<>();
            boolean hasAddition = false;
            while (hasNextArg()) {
                addition = nextArg();
                try {
                    arr.add(Integer.parseInt(addition));
                } catch (NumberFormatException e) {
                    hasAddition = true;
                    break;
                }
            }
            if (!hasAddition) addition = "";
            return Global.cmdManPrivate.execute(
                    "makeVote",
                    voteId, Global.gson.toJson(arr.stream().mapToInt(Integer::intValue).toArray()), addition
            );
        } catch (NullPointerException e) {
            Sys.err("Vote", "Missing parameters. Type 'help' for more information.");
            return null;
        }
    }

    private CmdResponder getResult() throws IndexOutOfBoundsException {
        return Global.cmdManPrivate.execute(
                "fetchVoteResult",
                nextArg()
        );
    }

    private CmdResponder addVote() throws IndexOutOfBoundsException {
        try {
            Scanner sc = CmdLineAdapter.sc;
            Sys.clearLine();
            Sys.cmdLinePrint("Please input title: "); String title = sc.nextLine();
            Sys.cmdLinePrint("Please input content: "); String content = sc.nextLine();
            Sys.cmdLinePrint("Please input selection Number: "); String numberStr = sc.nextLine();
            int number = Integer.parseInt(numberStr);
            String[] selections = new String[number];
            for (int i = 0; i < number; i++) {
                Sys.cmdLinePrintF("Please input arg %d / %d: ", i + 1, number);
                selections[i] = sc.nextLine();
            }
            Sys.cmdLinePrint("Please input Exceed time (if always valid, please input 'never'): ");
            String validity = sc.nextLine();
            if (validity.equals("never")) validity = "2000-01-01 00:00:00";

            Sys.cmdLinePrint("Please input method (0 = single, 1 = multiple): "); String method = sc.nextLine();
            Sys.cmdLinePrint("Please input class id: "); String classId = sc.nextLine();
            Sys.cmdLinePrint("Please input current status (0 = valid, 1 = canceled): ");
            String status = sc.nextLine();
            Sys.cmdLinePrint("Please input access level (0 = normal students, 1 = normal managers, " +
                    "2 = super admins): ");
            String level = sc.nextLine();
            Sys.cmdLinePrint("Data input over, pre-checking...");
            if (content.length() == 0) throw new Exception("Vote must have a content.");
            if (title.length() == 0) throw new Exception("Vote must have a title.");
            String selection = Global.gson.toJson(selections);
            if (selection.length() == 0) throw new Exception("Vote must have a selection.");
            new DateTime(validity);
            int lev = Integer.parseInt(method);
            if (lev != 0 && lev != 1) throw new Exception("Method must be 0 or 1.");
            Integer.parseInt(classId);
            lev = Integer.parseInt(status);
            if (lev != 0 && lev != 1) throw new Exception("Status must be 0 or 1.");
            lev = Integer.parseInt(level);
            if (lev != 0 && lev != 1 && lev != 2) throw new Exception("Level must be a digit within 0-2.");
            Sys.clearLine();
            Sys.cmdLinePrint("Data pre-check ok, sending to master server...");
            return Global.cmdManPrivate.execute(
                    "addVote",
                    title, content, selection, validity, method, classId, level, status
            );
        } catch (Exception e) {
            Sys.err("Add Vote", e.getMessage());
            return null;
        }
    }

    private CmdResponder alterVote() throws IndexOutOfBoundsException {
        try {
            Scanner sc = CmdLineAdapter.sc;
            Sys.clearLine();
            Sys.cmdLinePrint("Please input Vote id: "); int id = Integer.parseInt(sc.nextLine());
            Sys.cmdLinePrint("Now fetching the Vote...");
            Global.setVar("VOTE_INFO_INTERNAL", true);
            Global.cmdManPrivate.execute("fetchVoteInfo", String.valueOf(id));
            while (Global.varExists("VOTE_INFO_INTERNAL")) { }
            FetchInfoResult res = Global.getVar("VOTE_INFO_CACHE", FetchInfoResult.class);
            Global.dropVar("VOTE_INFO_CACHE");
            if (res == null) {
                Sys.err("Alter Vote", "Vote id not exist.");
                return null;
            }
            Sys.clearLine();
            Sys.cmdLinePrintln("Now gathering information, if you want to remain unchanged, don't type anything.");
            Sys.cmdLinePrintF("Please input title (%s): ", res.title); String title = sc.nextLine();
            if (title.length() == 0) title = res.title;

            Sys.cmdLinePrintF("Please input content (%s): ", res.content); String content = sc.nextLine();
            if (content.length() == 0) content = res.content;

            Sys.cmdLinePrint("Please input selection Number :"); String numberStr = sc.nextLine();
            String selection = res.selections;
            if (numberStr.length() != 0) {
                int number = Integer.parseInt(numberStr);
                String[] selections = new String[number];
                for (int i = 0; i < number; i++) {
                    Sys.cmdLinePrintF("Please input arg %d / %d: ", i + 1, number);
                    selections[i] = sc.nextLine();
                }
                selection = Global.gson.toJson(selections);
            }
            boolean isNever = res.validity.smallerThan(res.date);
            Sys.cmdLinePrintF("Please input Exceed time (%s): ", isNever ? "never" : res.validity);
            String validity = sc.nextLine();
            if (validity.equals("never")) validity = "2000-01-01 00:00:00";
            if (validity.length() == 0) {
                validity = res.validity.toString();
            } else {
                new DateTime(validity);
            }

            Sys.cmdLinePrintF("Please input method (0 = single, 1 = multiple) (Now is %d): ", res.method); String method = sc.nextLine();
            if (method.length() == 0) method = String.valueOf(res.level);
            else {
                int lev = Integer.parseInt(method);
                if (lev != 0 && lev != 1) throw new Exception("Method must be a digit within 0-2.");
            }

            Sys.cmdLinePrintF("Please input class id (%d): ", res.classId); String classId = sc.nextLine();
            if (classId.length() == 0) classId = String.valueOf(res.classId);
            else Integer.parseInt(classId);

            Sys.cmdLinePrintF("Please input current status (0 = valid, 1 = canceled) (now is %d): ", res.status);
            String status = sc.nextLine();
            if (status.length() == 0) status = String.valueOf(res.status);
            else {
                int lev = Integer.parseInt(status);
                if (lev != 0 && lev != 1) throw new Exception("Status must be 0 or 1.");
            }

            Sys.cmdLinePrintF("Please input access level (0 = normal students, 1 = normal managers, " +
                    "2 = super admins) (now is %d): ", res.level);
            String level = sc.nextLine();
            if (level.length() == 0) level = String.valueOf(res.level);
            else {
                int lev = Integer.parseInt(level);
                if (lev != 0 && lev != 1 && lev != 2) throw new Exception("Level must be a digit within 0-2.");
            }

            Sys.cmdLinePrint("Data pre-check ok, sending to master server...");
            return Global.cmdManPrivate.execute(
                    "alterVote",
                    String.valueOf(id), title, content, selection, validity, method, classId, level, status
            );
        } catch (Exception e) {
            Sys.err("Alter Vote", e.getMessage());
            return null;
        }
    }

    private CmdResponder delVote() throws IndexOutOfBoundsException {
        if (hasNextArg()) {
            try {
                String arg = nextArg();
                Integer.parseInt(arg);
                return Global.cmdManPrivate.execute(
                        "delVote",
                        arg
                );
            } catch (Exception e) {
                Sys.err("Del Vote", "Something is wrong with Vote id.");
                return null;
            }
        } else {
            Sys.err("Del Vote", "Vote id is required.");
        }
        return null;
    }

    @Override
    public String getManual() {
        return "To make actions that relevant to voting system. \n " +
                "\t\t- ls\t\tList all the votes that relevant to you.\n" +
                "\t\t- get [voteId]\t\tGet the vote information by id.\n" +
                "\t\t- make [voteId] [select 1] [select 2] ...\t\t Make your vote. " +
                "If is single vote, there is only 1 selection allowed.\n" +
                "\t\t- result [voteId]\t\tGet the results of a vote.\n" +
                "\t\t- add\t\tAdd a new vote. There will be a guidance system guiding you create a vote.\n" +
                "\t\t- alter\t\tAlter a vote. There will be a guidance system guiding you create a vote.\n" +
                "\t\t- del [voteId]\t\tDelete a vote. Warning: this action is irreversible.";
    }

}
