package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/*
 *
 * gen by beetlsql 2020-04-20
 * @author tiza
 */
@Getter
@Setter
public class AfterPaidQuery extends Query {
    @NotNull
    private Long beginTime;
    @NotNull
    private Long endTime;

    @Override
    protected void convertParams() {
        add("beginTime", this.beginTime);
        add("endTime", this.endTime);
    }

}
