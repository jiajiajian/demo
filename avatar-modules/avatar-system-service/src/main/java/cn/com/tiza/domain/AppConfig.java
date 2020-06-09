package cn.com.tiza.domain;

import lombok.Data;

/**
 * @author tiza
 */
@Data
public class AppConfig {

    private Integer msgFlag;

    private Integer alarmFlag;

    private Integer faultFlag;

    private Integer obdFlag;

    private Integer dischargeFlag;

    private Integer terminalFlag;

    /**
     * 系统语言
     */
    private String sysLang;
}
