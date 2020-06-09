package cn.com.tiza.web.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
public class FunctionSetItemVM {

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
    private String description;

    private String varAddress;

    private String separator;

    private Integer sortNum;

    public FunctionSetItemVM() {
    }


}
