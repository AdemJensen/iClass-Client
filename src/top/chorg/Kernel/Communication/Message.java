package top.chorg.Kernel.Communication;

import top.chorg.Support.SerializableMap;

import java.io.Serializable;

public class Message implements Serializable {
    public String msgType;
    public Serializable content;

    public Message() {}

    public Message(String msgType, Serializable content) {
        this.msgType = msgType;
        this.content = content;
    }

    public boolean validate(String val) {
        String validationString = "Oxford";
        return validationString.equals(val);
    }
}
