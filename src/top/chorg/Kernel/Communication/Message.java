package top.chorg.Kernel.Communication;

import java.io.Serializable;

public class Message implements Serializable {
    public String msgType;
    public Serializable content;

    public boolean validate(String val) {
        String validationString = "Oxford";
        return validationString.equals(val);
    }
}
