package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Getter
@Setter
public class FunctionSetQuery extends Query {

    private String keyword;

    /**
     * 1采集功能集 2锁车
     */
    private Integer functionType;

    @Override
    protected void convertParams() {
         add("keyword", this.keyword, true);
         add("functionType", this.functionType);
    }

}
