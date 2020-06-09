package cn.com.tiza.protocol.one;

import cn.com.tiza.protocol.AbstractCmd;
import cn.com.tiza.util.CmdConstant;

import java.util.Map;

public class Cmd_2C extends AbstractCmd {

    public Cmd_2C() {
        //ACC
        this.cmdId = CmdConstant.WORK_PARAM_SET;
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
