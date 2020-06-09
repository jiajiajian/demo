package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author tz0920
 */
@Data
public class TerminalInfoVM {
    private String vin;
    private Long terminalId;
    private String terminalCode;
    private String simCard;
}
