package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.Table;

/**
 * 字典表
 */
@Data
@Table(name = "base_dictionary")
public class BaseDictionary {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 代码
     */
    private String itemCode;
    /**
     * 名称
     */
    private String itemName;
    /**
     * 上级字典项ID
     */
    private Long parentDicId;
    /**
     * 排序码
     */
    private String sortCode;
    /**
     * 所属机构ID
     */
    private Long organizationId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 创建用户登录名
     */
    private String createUserAccount;
    /**
     * 创建用户姓名
     */
    private String createUserRealname;
    /**
     * 更新用户登录名
     */
    private String updateUserAccount;
    /**
     * 更新用户姓名
     */
    private String updateUserRealname;
    /**
     * 停启用状态
     */
    private Boolean enableFlag;
    /**
     * 逻辑删除标记
     */
    private Boolean delFlag;
    /**
     * 是否可作为监控项 1是 0否，默认状态0
     */
    private Integer monitorStatus;
    /**
     * 扩展字段
     */
    private String expand;

}
