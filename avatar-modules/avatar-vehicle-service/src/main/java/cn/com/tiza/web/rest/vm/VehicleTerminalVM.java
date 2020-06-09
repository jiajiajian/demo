package cn.com.tiza.web.rest.vm;

import lombok.Data;

@Data
public class VehicleTerminalVM {
    private Long id;

    /**
     * 机器序列号
     */
    private String vin;

    /**
     * 机构
     */
    private Long organizationId;
    private String orgName;
    private String terminalCode;
    private String simCard;

    /**
     * ---业务日志需要字段
     */
    private String oldVin;
    private String oldTerminal;
    private String oldSimCard;
    /**
     * 续费月份
     */
    private Integer renewalMon;
}
