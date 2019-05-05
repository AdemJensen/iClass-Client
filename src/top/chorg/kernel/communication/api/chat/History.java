package top.chorg.kernel.communication.api.chat;

import top.chorg.support.DateTime;

public class History {
    public int id, fromId, type, toId;
    public DateTime time;
    public String content;

    public History(int id, int fromId, int type, int toId, DateTime time, String content) {
        this.id = id;
        this.fromId = fromId;
        this.type = type;
        this.toId = toId;
        this.time = time;
        this.content = content;
    }

    public String getTargetStr() {
        return String.format("%s %d", type == 1 ? "class" : "user", toId);
    }
}
