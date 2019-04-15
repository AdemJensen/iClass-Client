package top.chorg.kernel.communication.api.announcements;

import top.chorg.support.DateTime;

public class FetchListResult {
    public int id;
    public String title, content;
    public DateTime date, validity;
    public int classId, level, publisher, status;

    public FetchListResult(int id, String title, String content, String date, String validity,
                           int classId, int level, int publisher, int status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = new DateTime(date);
        this.validity = new DateTime(validity);
        this.classId = classId;
        this.level = level;
        this.publisher = publisher;
        this.status = status;
    }
}
