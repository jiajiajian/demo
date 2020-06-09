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
@Table(name = "t_soft_version")
public class SoftVersion implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 采集功能集ID
     */
    private Long collectFunctionId;
    private Long createTime;
    /**
     * 锁车功能集ID
     */
    private Long lockFunctionId;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String remark;

    public SoftVersion() {
    }

    private String collectName;

    private String lockName;

}
