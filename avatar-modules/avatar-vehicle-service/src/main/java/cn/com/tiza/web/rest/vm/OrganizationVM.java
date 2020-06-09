package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 *
 */
@Data
public class OrganizationVM {

    private Long id;
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;

    private String abbrName;
}
