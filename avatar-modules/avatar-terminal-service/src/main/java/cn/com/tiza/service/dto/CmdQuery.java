package cn.com.tiza.service.dto;

import lombok.Data;

@Data
public class CmdQuery {

    /**
     * 指令类型
     */
    private Long functionLockId;
    /**
     * Vin
     */
    private String vin;

    public CmdQuery() {
    }
}
