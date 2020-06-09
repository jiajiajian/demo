package cn.com.tiza.protocol.one;

import cn.com.tiza.protocol.AbstractCmd;
import cn.com.tiza.util.CmdConstant;

import java.util.Map;

public class Cmd_18 extends AbstractCmd {

    public Cmd_18() {
        //查询工况
        this.cmdId = CmdConstant.TOTAL_DATA;
    }

    @Override
    protected Map<String, Object> resSuccess(byte[] body) {
        return null;
    }

    @Override
    protected int getStatus(byte[] bytes) {
        return 0;
    }
}
