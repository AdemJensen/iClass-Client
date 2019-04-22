package top.chorg.kernel.cmd.privateResponders.auth;

import top.chorg.kernel.cmd.CmdResponder;

public class PromoteUser extends CmdResponder {
    public PromoteUser(String... args) {
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
