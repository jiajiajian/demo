package cn.com.tiza.domain;

import cn.com.tiza.web.rest.errors.BadRequestException;
import lombok.Data;

/**
 * @author villas
 */
@Data
public class CmdRes {

    /**
     * 响应报文
     */
    private byte[] body;

    /**
     * 0：成功  1：失败  2：超时  3：执行中  4：离线指令已发送',
     */
    private int state;

    public String getStatus() {
        return getStatus(this.state);
    }

    public static String getStatus(int state) {
        switch (state) {
            case 0:
                return "成功";
            case 1:
                return "失败";
            case 2:
                return "超时";
            case 3:
                return "执行中";
            case 4:
                return "离线指令已发送";
            case 5:
                return "其他";
            case 7:
                return "指令已发送终端";
            default:
                throw new BadRequestException("invalid state" + state);
        }
    }
}
