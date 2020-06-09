package cn.com.tiza.protocol.one;

import cn.com.tiza.protocol.AbstractCmd;
import cn.com.tiza.util.CmdConstant;

import java.util.Map;

public class Cmd_48 extends AbstractCmd {
    public Cmd_48() {
        this.cmdId = CmdConstant.RELAY_LOCK;
    }

    @Override
    protected Map<String, Object> resSuccess(byte[] body) {
        return null;
    }

    @Override
    protected int getStatus(byte[] bytes) {
        return bytes[14];
    }
}
