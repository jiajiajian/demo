package cn.com.tiza.protocol.one;

import cn.com.tiza.constant.GeneralParamEnum;
import cn.com.tiza.protocol.AbstractCmd;
import cn.com.tiza.util.CmdConstant;
import cn.com.tiza.util.ToolUtil;
import cn.com.tiza.web.rest.errors.BadRequestException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static cn.com.tiza.util.MessageUtil.*;

@Slf4j
public class Cmd_11 extends AbstractCmd {

    public Cmd_11() {
        this.cmdId = CmdConstant.PARAM_QUERY;
    }

    @Override
    protected Map<String, Object> resSuccess(byte[] result) {
        Map<String, Object> resMap = new HashMap<>(ToolUtil.computeMap(GeneralParamEnum.values().length));
        int pos = 11;
        do {
            boolean flag = result[pos++] == 0;
            int paramId = getUnsignedShort(subBytes(result, pos, 2));
            pos += 2;
            int paramLen = getUnsignedByte(result[pos++]);
            if (flag) {
                byte[] valueBytes = subBytes(result, pos, paramLen);
                pos += paramLen;
                GeneralParamEnum paramEnum = GeneralParamEnum.fromId(paramId).orElseThrow(() -> new BadRequestException(""));
                switch (paramEnum) {
                    case HOST_IP:
                        resMap.put(paramEnum.name(), parseIp(valueBytes));
                        break;

                    case DEPUTY_IP:
                        resMap.put(paramEnum.name(), parseIp(valueBytes));
                        break;

                    case PORT:
                        resMap.put(paramEnum.name(), getUnsignedShort(valueBytes));
                        break;

                    case ACC_SUM_TIME:
                        resMap.put(paramEnum.name(), getUnsignedInt(valueBytes));
                        break;

                    case IS_SLEEP:
                        resMap.put(paramEnum.name(), getUnsignedByte(valueBytes[0]));
                        break;

                    case ACC_OPEN_CLOSE:
                        resMap.put(paramEnum.name(), getUnsignedShort(subBytes(valueBytes, 0, 2)) + ":"
                                + getUnsignedShort(subBytes(valueBytes, 2, 2)));
                        break;
                    case SOFTWARE_VERSION:
                        resMap.put(paramEnum.name(), new String(valueBytes));
                        break;
                    default:
                        throw new BadRequestException("general param is not support");
                }
            }
        } while (pos < result.length);
        return resMap;
    }

    @Override
    protected int getStatus(byte[] bytes) {
        return bytes[11];
    }

    /**
     * 解析ip地址
     */
    private static String parseIp(byte[] ipByte) {
        try {
            InetAddress inetAddress = InetAddress.getByAddress(ipByte);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("parseIp error: {}", e.getLocalizedMessage());
            throw new BadRequestException("terminal.body.invalid");
        }
    }
}
