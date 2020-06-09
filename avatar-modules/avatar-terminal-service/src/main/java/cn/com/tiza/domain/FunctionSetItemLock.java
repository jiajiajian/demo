package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Data
@Table(name = "t_function_set_item_lock")
public class FunctionSetItemLock implements Serializable {

    @AutoID
    @AssignID("simple")
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

    private String message;

    public FunctionSetItemLock() {
    }


    private String typeName;

    private String itemCode;


}
