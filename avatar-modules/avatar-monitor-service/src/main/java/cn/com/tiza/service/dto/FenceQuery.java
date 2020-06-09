package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Getter
@Setter
public class FenceQuery extends Query {
    /**
     * 1:普通机构 2:融资机构
     */
    private Integer orgType;
    private String fenceName;

    @Override
    protected void convertParams() {
        add("fenceName", this.fenceName, true);
        add("orgType", this.orgType);
    }

}
