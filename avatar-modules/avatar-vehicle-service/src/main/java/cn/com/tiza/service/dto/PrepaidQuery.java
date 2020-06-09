package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/*
 *
 * gen by beetlsql 2020-04-20
 * @author tiza
 */
@Getter
@Setter
public class PrepaidQuery extends Query {
    private Integer beginDate;
    private Integer endDate;
    private Long beginTime;
    private Long endTime;

    @Override
    protected void convertParams() {
        add("beginDate", this.beginDate);
        add("endDate", this.endDate);
        add("beginTime", this.beginTime);
        add("endTime", this.endTime);
    }

}
