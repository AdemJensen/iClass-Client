package top.chorg.kernel.communication.api;

import com.google.gson.JsonSyntaxException;
import top.chorg.system.Global;

public class Message {
    private String msgType;
    private String content;

    public Message() {}

    public Message(String msgType, String content) {
        this.msgType = msgType;
        this.content = content;
    }

    public static Message decode(String str) {
        try {
            return Global.gson.fromJson(str, Message.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public String getMsgType() {
        return msgType;
    }

    public String getContent() {
        return this.content;
    }

    public <T> T getContent(Class<T> cls) {
        return Global.gson.fromJson(content, cls);
    }

    public String encode() {
        return Global.gson.toJson(this, this.getClass());
    }

}
