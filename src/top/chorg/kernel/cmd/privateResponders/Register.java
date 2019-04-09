package top.chorg.kernel.cmd.privateResponders;

import top.chorg.kernel.cmd.CmdResponder;

public class Register extends CmdResponder {

    public Register(String[] args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        return super.onReceiveNetMsg();
    }
}
