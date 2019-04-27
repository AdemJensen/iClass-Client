package top.chorg.kernel.communication.api.chat;

import top.chorg.support.DateTime;

public class ChatMsg {
    public int id;
    public int fromId;  // From userId.
    public int type;   // 1 = to class, 2 = to individual
    public int toId;   // if type = 1, then classId. if type = 2, then userId.
    public DateTime time;
    public String content;

    public ChatMsg(int type, int toId, String content) {
        this.type = type;
        this.toId = toId;
        this.content = content;
    }

    public ChatMsg(int id, int fromId, int type, int toId, DateTime time, String content) {
        this.id = id;
        this.fromId = fromId;
        this.type = type;
        this.toId = toId;
        this.time = time;
        this.content = content;
    }

    public String getSenderStr() {
        return String.format("%s %d", type == 1 ? "class" : "user", toId);
    }
}
