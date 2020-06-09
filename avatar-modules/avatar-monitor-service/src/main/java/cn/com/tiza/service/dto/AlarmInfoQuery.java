package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/*
* 
* gen by beetlsql 2020-03-23
* @author tiza
*/
@Getter
@Setter
public class AlarmInfoQuery extends Query {
    private String vin;

    @Override
    protected void convertParams() {
        add("vin",this.vin);
    }

}
