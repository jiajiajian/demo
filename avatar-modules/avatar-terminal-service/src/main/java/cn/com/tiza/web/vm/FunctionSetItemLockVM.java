package cn.com.tiza.web.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Data
public class FunctionSetItemLockVM {

    /**
     * id
     */
    private Long id;
    /**
     * 中文名称
     */
    private String chinaName;
    /**
     * key标识
     */
    private String code;
    /**
     * 变量类型---对应字典表(item表)中的id
     */
    private Long dicItemId;
    /**
     * 英文名称
     */
    private String englishName;

    /**
     * 功能集ID
     */
    private Long functionId;

    private String typeName;

    private String message;

    public FunctionSetItemLockVM() {
    }


}
