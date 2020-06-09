package cn.com.tiza.service.dto;

import lombok.Data;

/**
 * General通用得信息
 *
 * @author villas
 */
@Data
public class GeneralDto {

    private String vin;

    private String sim;

    private String terminalCode;

    private String protocol;

    private String protocolType;
    /**
     * 执行状态：0：成功  1：失败  2：超时  3：执行中  4：离线指令已发送
     */
    private Integer runState;

    /**
     * 是否可以发送指令
     *
     * @return boolean
     */

    public boolean isCanSendCmd() {
        //只有在执行状态为最终状态(0,1,2)或者未发送(null)时可以发送指令
        return this.runState == null || this.runState == 0 || this.runState == 1 || this.runState ==2;
    }

}
