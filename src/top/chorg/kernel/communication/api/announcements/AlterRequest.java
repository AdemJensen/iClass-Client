package top.chorg.kernel.communication.api.announcements;

import top.chorg.support.DateTime;

public class AlterRequest {
    public String title, content;
    public DateTime validity;
    public int id, classId, level, status;

    public AlterRequest(int id, String title, String content, DateTime validity,
                        int classId, int level, int status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.validity = validity;
        this.classId = classId;
        this.level = level;
        this.status = status;
    }
}
