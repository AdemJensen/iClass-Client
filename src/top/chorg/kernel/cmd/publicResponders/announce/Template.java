package top.chorg.kernel.cmd.publicResponders.announce;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.api.announcements.FetchTemplateResult;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Scanner;

public class Template extends CmdResponder {

    public Template(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            String cmd = nextArg();
            CmdResponder resp;
            switch (cmd) {
                case "ls":
                    resp = fetchTemplateList();
                    break;
                case "add":
                    resp = addTemplate();
                    break;
                case "alter":
                    resp = alterTemplate();
                    break;
                case "del":
                    resp = delTemplate();
                    break;
                default:
                    Sys.errF("Template", "Cmd '%s' is invalid.", cmd);
                    return 1;
            }
            if (resp == null) {
                Sys.err("Template", "Something went wrong when executing commands.");
                return 2;
            }
            while (resp.isAlive()) {}
        } else {
            Sys.err("Template", "User is not online, please login first.");
        }
        return 0;
    }

    private CmdResponder fetchTemplateList() throws IndexOutOfBoundsException {
        return Global.cmdManPrivate.execute(
                "fetchAnnounceTemplate",
                ""
        );
    }

    private CmdResponder addTemplate() throws IndexOutOfBoundsException {
        Scanner sc = CmdLineAdapter.sc;
        Sys.clearLine();
        Sys.cmdLinePrint("Please input name: "); String name = sc.nextLine();
        Sys.cmdLinePrint("Please input title: "); String title = sc.nextLine();
        Sys.cmdLinePrint("Please input content: "); String content = sc.nextLine();
        Sys.cmdLinePrint("Data input over, pre-checking...");
        try {
            if (name.length() == 0) throw new Exception("Template must have a name.");
            if (title.length() == 0) throw new Exception("Template must have a title.");
            if (content.length() == 0) throw new Exception("Template must have a content.");
        } catch (Exception e) {
            Sys.err("Add Template", e.getMessage());
            return null;
        }
        Sys.clearLine();
        Sys.cmdLinePrint("Data pre-check ok, sending to master server...");
        return Global.cmdManPrivate.execute(
                "addAnnounceTemplate",
                name, title, content
        );
    }

    private CmdResponder alterTemplate() throws IndexOutOfBoundsException {
        Scanner sc = CmdLineAdapter.sc;
        Sys.clearLine();
        Sys.cmdLinePrint("Please input template id: "); int id = Integer.parseInt(sc.nextLine());
        Sys.cmdLinePrint("Now fetching the templates...");
        Global.setVar("TEMPLATE_LIST_INTERNAL", true);
        Global.cmdManPrivate.execute("fetchAnnounceTemplate", "");
        while (Global.varExists("TEMPLATE_LIST_INTERNAL")) { }
        FetchTemplateResult[] temp = Global.getVar("TEMPLATE_LIST_CACHE", FetchTemplateResult[].class);
        Global.dropVar("TEMPLATE_LIST_CACHE");
        if (temp == null) {
            Sys.err("Alter Template", "Error while fetching template.");
            return null;
        }
        FetchTemplateResult res = null;
        for (FetchTemplateResult result : temp) {
            if (result.id == id) {
                res = result;
                break;
            }
        }
        if (res == null) {
            Sys.err("Alter Template", "Template id not exist.");
            return null;
        }
        Sys.clearLine();
        Sys.cmdLinePrintln("Now gathering information, if you want to remain unchanged, don't type anything.");
        Sys.cmdLinePrintF("Please input name (%s): ", res.name); String name = sc.nextLine();
        Sys.cmdLinePrintF("Please input title (%s): ", res.title); String title = sc.nextLine();
        Sys.cmdLinePrintF("Please input content (%s): ", res.content); String content = sc.nextLine();
        Sys.cmdLinePrint("Data input over, pre-checking...");
        try {
            if (name.length() == 0) name = res.name;
            if (title.length() == 0) title = res.title;
            if (content.length() == 0) content = res.content;
        } catch (Exception e) {
            Sys.err("Alter Template", e.getMessage());
            return null;
        }
        Sys.clearLine();
        Sys.cmdLinePrint("Data pre-check ok, sending to master server...");
        return Global.cmdManPrivate.execute(
                "alterAnnounceTemplate",
                String.valueOf(id), name, title, content
        );
    }

    private CmdResponder delTemplate() throws IndexOutOfBoundsException {
        if (hasNextArg()) {
            try {
                String arg = nextArg();
                Integer.parseInt(arg);
                return Global.cmdManPrivate.execute(
                        "delAnnounceTemplate",
                        arg
                );
            } catch (Exception e) {
                Sys.err("Del Template", "Something is wrong with template id.");
                return null;
            }
        } else {
            Sys.err("Del Template", "Template id is required.");
        }
        return null;
    }

    @Override
    public String getManual() {
        return "";
    }


}
