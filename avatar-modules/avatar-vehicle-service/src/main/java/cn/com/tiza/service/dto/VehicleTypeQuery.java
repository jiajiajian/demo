package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Getter
@Setter
public class VehicleTypeQuery extends Query {
    private String name;

    @Override
    protected void convertParams() {
        add("name", this.name, true);
    }

}
