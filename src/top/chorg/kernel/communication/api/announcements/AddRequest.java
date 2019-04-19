package top.chorg.kernel.communication.api.announcements;

import top.chorg.support.DateTime;

public class AddRequest {
    public String title, content;
    public DateTime validity;
    public int classId, level, status;

    public AddRequest(String title, String content, DateTime validity, int classId, int level, int status) {
        this.title = title;
        this.content = content;
        this.validity = validity;
        this.classId = classId;
        this.level = level;
        this.status = status;
    }
}
