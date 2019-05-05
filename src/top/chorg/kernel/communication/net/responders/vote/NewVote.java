package top.chorg.kernel.communication.net.responders.vote;

import top.chorg.cmdLine.CmdLineAdapter;
import top.chorg.kernel.communication.api.vote.FetchInfoResult;
import top.chorg.kernel.communication.net.NetResponder;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.util.Arrays;

public class NewVote extends NetResponder {
    public NewVote(String obj) {
        super(obj);
    }

    @Override
    public int response() {
        FetchInfoResult result = getArg(FetchInfoResult.class);
        Global.guiAdapter.makeEvent("onNewVote", Global.gson.toJson(result));
        Sys.info("New Vote", "New vote received:");
        return displayVoteInfo(result);
    }

    public static int displayVoteInfo(FetchInfoResult result) {
        Sys.clearLine();
        Sys.cmdLinePrintF(
                "%12s | %d\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %d\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n" +
                        "%12s | %s\n",
                "id", result.id,
                "title", result.title,
                "content", result.content,
                "selections", result.selections,
                "date", result.date.toString(),
                "validity", result.validity.toString(),
                "method", result.method,
                "class", result.classId,
                "level", result.level,
                "publisher", result.publisher,
                "status(self)", result.status,
                "isVoted", result.isVoted,
                "Operation", Arrays.toString(result.ops),
                "Addition", result.addition
        );
        CmdLineAdapter.outputDecoration();
        return 0;
    }
}