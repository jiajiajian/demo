package cn.com.tiza.protocol.one;

import cn.com.tiza.protocol.AbstractCmd;
import cn.com.tiza.util.CmdConstant;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Cmd_10 extends AbstractCmd {

    public Cmd_10() {
        this.cmdId = CmdConstant.UPGRADE;
    }

    private Map<String, Object> resultOfSuccess;

    @Override
    protected Map<String, Object> resSuccess(byte[] body) {
        return this.resultOfSuccess;
    }

    @Override
    protected int getStatus(byte[] bytes) {
        boolean state;
        if (bytes.length == 15 && bytes[13] == this.cmdId) {
            //71命令应答
            state = bytes[14] == 0;
            this.resultOfSuccess = ImmutableMap.of("result", "终端应答" + (state ? "成功" : "失败"));
            return bytes[14];
        } else {
            //7D指令响应
            state = bytes[11] == 0;
            this.resultOfSuccess = ImmutableMap.of("result", "升级" + (state ? "成功" : "失败"));
            return bytes[11];
        }
    }
}
