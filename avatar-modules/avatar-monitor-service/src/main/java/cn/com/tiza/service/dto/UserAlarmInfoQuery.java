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
public class UserAlarmInfoQuery extends Query {
    private Long userId;

    @Override
    protected void convertParams() {
         add("userId", this.userId);
    }

}
