package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-04-16
 *
 * @author tiza
 */
@Getter
@Setter
public class CmdLogQuery extends Query {

    private Long beginTime;
    private Long endTime;
    private String vin;

    @Override
    protected void convertParams() {
        add("beginTime", this.beginTime);
        add("endTime", this.endTime);
        add("vin", this.vin);
    }

}
