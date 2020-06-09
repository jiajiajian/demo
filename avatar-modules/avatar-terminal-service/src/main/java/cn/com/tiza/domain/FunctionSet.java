package cn.com.tiza.domain;

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
@Table(name = "t_function_set")
public class FunctionSet implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 编号
     */
    private String code;
    private Long createTime;
    private String createUserAccount;
    /**
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

    public FunctionSet() {
    }

}
