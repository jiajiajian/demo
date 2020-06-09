package cn.com.tiza.dto;

import lombok.Data;

@Data
public class CommandDto {
    /**
     * 指令值10进制
     */
    private Integer cmdId;
    /**
     * 指令序列号
     */
    private Integer serialNo;
    /**
     * 状态 0：成功  1：失败  2：超时  3：执行中  4：离线指令已发送 5: 其他 7：指令已发送终端
     */
    private Integer state;
    /**
     * 响应报文（base64加密）
     */
    private String resBody;
    /**
     * 请求报文（base64加密）
     */
    private String reqBody;
    /**
     * 指令类型 0：实时指令，1离线指令
     */
    private Integer cmdType;
    /**
     * 原因
     */
    private String reason;
    /**
     * 备注、其他
     */
    private String remark;
    /**
     * 指令响应时间
     */
    private Long resTime;
    /**
     * 指令发送时间
     */
    private Long sendTime;
    /**
     * vin码
     */
    private String vin;

}
