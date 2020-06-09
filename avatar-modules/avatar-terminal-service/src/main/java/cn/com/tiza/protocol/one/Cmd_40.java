package cn.com.tiza.protocol.one;

import cn.com.tiza.protocol.AbstractCmd;
import cn.com.tiza.util.CmdConstant;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Cmd_40 extends AbstractCmd {

    public Cmd_40() {
        this.cmdId = CmdConstant.CONTROLLER_LOCK;
    }

    private Map<String, Object> resultOfSuccess;

    @Override
    protected Map<String, Object> resSuccess(byte[] body) {
        return this.resultOfSuccess;
    }

    @Override
    protected int getStatus(byte[] bytes) {
        String res;
        if (bytes.length == 15 && bytes[13] == this.cmdId) {
            //71命令应答
            int state = bytes[14];
            res = state == 0 ? "成功" : "失败";
            this.resultOfSuccess = ImmutableMap.of("result", "终端应带" + res);
            return state;
        } else {
            //84指令响应
            int state = bytes[11];
            res = state == 0 ? "成功" : "失败";
            this.resultOfSuccess = ImmutableMap.of("result", "指令执行" + res);
            return bytes[11];
        }
    }
}
