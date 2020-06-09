package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Getter
@Setter
public class VehicleModelWorkTimeQuery  extends  Query{
    @NotNull
    private int year;

    private int month;

    @Override
    protected void convertParams() {
        add("year", this.year);

    }
}
