package top.chorg.Kernel.Cmd.PrivateResponders;

import top.chorg.Kernel.Cmd.CmdResponder;

import java.io.Serializable;

public class TokenLoginResponder extends CmdResponder {

    public TokenLoginResponder(Serializable args) {
        super(args);
    }

    @Override
    public int response() {
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        return 0;
    }

    @Override
    public String getManual() {
        return null;
    }
}
