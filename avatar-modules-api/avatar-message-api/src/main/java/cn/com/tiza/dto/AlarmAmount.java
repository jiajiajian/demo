package cn.com.tiza.dto;

import lombok.Data;

/**
 * @author tiza
 */
@Data
public class AlarmAmount {

    private Integer faults;
    private Integer alarms;
    private Integer discharge;
    private Integer obd;
    private Integer terminal;
}
