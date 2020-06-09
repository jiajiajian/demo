package cn.com.tiza.service.dto;

import lombok.Data;


/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
public class FunctionSetDto {

    private Long id;
    /**
     * 编号
     */
    private String code;
    private Long createTime;
    private String createUserAccount;
    /**
     * 1.采集、2.锁车
     * 功能集类型
     */
    private Integer functionType;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String remark;

    public FunctionSetDto() {
    }


}
