package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/*
* 
* gen by beetlsql 2020-03-09
* @author tiza
*/
@Getter
@Setter
public class SoftVersionQuery extends Query {

    // private String fieldName;
    private String keywords;

    @Override
    protected void convertParams() {
        // add("fieldName", this.fieldName, true);
        add("keywords", this.keywords, true);
    }

}
