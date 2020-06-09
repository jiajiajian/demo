package cn.com.tiza.protocol.one;

import cn.com.tiza.protocol.AbstractCmd;
import cn.com.tiza.util.CmdConstant;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;

import java.util.Map;

import static cn.com.tiza.util.MessageUtil.subBytes;

public class Cmd_02 extends AbstractCmd {

    public Cmd_02() {
        this.cmdId = CmdConstant.LOCATION;
    }

    @Override
    public Map<String, Object> resSuccess(byte[] body) {
        int pos = 11;
        double lat = Ints.fromByteArray(subBytes(body, pos, 4)) / 1000000.0;
        pos += 4;
        double lon = Ints.fromByteArray(subBytes(body, pos, 4)) / 1000000.0;
        return ImmutableMap.of("lon", lon, "lat", lat);
    }

    @Override
    protected int getStatus(byte[] bytes) {
        return 0;
    }
}
