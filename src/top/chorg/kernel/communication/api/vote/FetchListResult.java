package top.chorg.kernel.communication.api.vote;

import top.chorg.support.DateTime;

public class FetchListResult {
    public int id;
    public String title;
    public DateTime date, validity;
    public int method, classId, level, status, publisher;
    public boolean isVoted;

    public FetchListResult(int id, String title, DateTime date, DateTime validity,
                           int method, int classId, int level, int status, int publisher, boolean isVoted) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.validity = validity;
        this.method = method;
        this.classId = classId;
        this.level = level;
        this.status = status;
        this.publisher = publisher;
        this.isVoted = isVoted;
    }
}
