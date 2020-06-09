package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-03-23
 *
 * @author tiza
 */
@Getter
@Setter
public class AlarmHistoryQuery extends Query {

    private String code;
    private String vin;
    private String alarmCode;
    private Integer alarmState;
    private Long beginTime;
    private Long endTime;
    private String spnFmi;
    private String tla;
    private Long rootOrg;
    private Boolean isTla;
    private Boolean isHistory;

    /**
     * 报警类型 1:报警 2:故障 3:围栏报警
     */
    private Integer alarmType;

    @Override
    protected void convertParams() {
        add("code", this.code, true);
        add("alarmCode", this.alarmCode, true);
        add("vin", this.vin);
        add("alarmState", this.alarmState);
        add("alarmType", this.alarmType);
        add("beginTime", this.beginTime);
        add("endTime", this.endTime);
        add("rootOrg", this.rootOrg);
        add("isTla", this.isTla);
        add("tla", this.tla);
        add("isHistory", this.isHistory);
        add("spnFmi", this.spnFmi, true);
    }

}
