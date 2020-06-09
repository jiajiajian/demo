package cn.com.tiza.protocol.one;

import cn.com.tiza.protocol.AbstractCmd;
import cn.com.tiza.util.CmdConstant;

import java.util.Map;

public class Cmd_05 extends AbstractCmd {

    public Cmd_05() {
        this.cmdId = CmdConstant.RELEASE_ALARM;
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
