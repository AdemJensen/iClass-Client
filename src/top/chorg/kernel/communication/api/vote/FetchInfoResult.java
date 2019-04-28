package top.chorg.kernel.communication.api.vote;

import top.chorg.support.DateTime;

public class FetchInfoResult {
    public int id;
    public String title, content, selections;
    public DateTime date, validity;
    public int method, classId, level, status, publisher;
    public boolean isVoted;
    public int[] ops;
    public String addition;

    public FetchInfoResult(int id, String title, String content, String selections, DateTime date, DateTime validity,
                           int method, int classId, int level, int status, int publisher, boolean isVoted, int[] ops,
                           String addition) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.selections = selections;
        this.date = date;
        this.validity = validity;
        this.method = method;
        this.classId = classId;
        this.level = level;
        this.status = status;
        this.publisher = publisher;
        this.isVoted = isVoted;
        this.ops = ops;
        this.addition = addition;
    }
}
