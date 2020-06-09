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
public class BusinessLogQuery extends Query {

    private String code;
    private Long beginTime;
    private Long endTime;
    private Integer operateType;

    @Override
    protected void convertParams() {
        add("code", this.code, true);
        add("beginTime", this.beginTime);
        add("endTime", this.endTime);
        add("operateType", this.operateType);
    }

}
