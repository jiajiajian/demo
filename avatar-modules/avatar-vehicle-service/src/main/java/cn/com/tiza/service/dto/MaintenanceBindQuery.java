package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
* gen by beetlsql 2020-04-01
* @author tiza
*/
@Getter
@Setter
public class MaintenanceBindQuery extends Query {

    // private String fieldName;

    @Override
    protected void convertParams() {
        // add("fieldName", this.fieldName, true);
    }

}
