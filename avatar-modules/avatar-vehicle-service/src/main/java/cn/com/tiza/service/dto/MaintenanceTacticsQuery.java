package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Getter
@Setter
public class MaintenanceTacticsQuery extends Query {

     private String tacticsName;

    @Override
    protected void convertParams() {
         add("tacticsName", this.tacticsName, true);
    }

}
