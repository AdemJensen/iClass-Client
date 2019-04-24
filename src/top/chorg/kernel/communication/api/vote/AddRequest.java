package top.chorg.kernel.communication.api.vote;

import top.chorg.support.DateTime;

public class AddRequest {
    public String title, content, selections;
    public DateTime validity;
    public int method, classId, level, status;

    public AddRequest(String title, String content, String selections, DateTime validity,
                      int method, int classId, int level, int status) {
        this.title = title;
        this.content = content;
        this.selections = selections;
        this.validity = validity;
        this.method = method;
        this.classId = classId;
        this.level = level;
        this.status = status;
    }
}
