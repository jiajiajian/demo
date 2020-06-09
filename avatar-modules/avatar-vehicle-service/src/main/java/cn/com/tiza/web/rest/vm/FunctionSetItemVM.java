package cn.com.tiza.web.rest.vm;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
public class FunctionSetItemVM implements Serializable {
    private Long id;
    /**
     * 位长度
     */
    private Integer bitLength;
    /**
     * 开始位
     */
    private Integer bitStart;
    /**
     * 编号
     */
    private String code;
    /**
     * Int/String
     */
    private String dataFormat;
    /**
     * 英文名称
     */
    private String enName;
    /**
     * 功能集ID
     */
    private Long functionId;
    /**
     * 名称
     */
    private String name;
    /**
     * 偏移量
     */
    private String offset;
    /**
     * 系数
     */
    private String rate;
    /**
     * 单位
     */
    private String unit;

    /**
     * 值
     */
    private String value;

    private String description;
    private String separator;
    private Integer sortNum;

}
