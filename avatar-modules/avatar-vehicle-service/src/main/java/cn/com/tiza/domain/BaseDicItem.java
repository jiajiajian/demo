package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-05-06
 *
 * @author tiza
 */
@Data
@Table(name = "base_dic_item")
public class BaseDicItem implements Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 是否可作为监控项 1是 0否，默认状态0
     */
    private Integer monitorStatus;
    /**
     * 计算脚本
     */
    private String caculateScript;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 创建用户登录名
     */
    private String createUserAccount;
    /**
     * 创建用户姓名
     */
    private String createUserRealname;
    /**
     * 数据字典项CODE
     */
    private String dicCode;
    /**
     * 代码
     */
    private String itemCode;
    /**
     * 名称
     */
    private String itemName;
    /**
     * 值
     */
    private String itemValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序码
     */
    private Integer sortCode;
    /**
     * 单位
     */
    private String unit;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 更新用户登录名
     */
    private String updateUserAccount;
    /**
     * 更新用户姓名
     */
    private String updateUserRealname;

    public BaseDicItem() {
    }

}
