package cn.com.tiza.domain;

import org.beetl.sql.core.annotatoin.LogicDelete;

/**
 * 基础Entity
 *
 * @author tiza
 */
public abstract class AbstractAuditingEntity extends AbstractEntity {

    /**
     * 停启用状态
     */
    private Boolean enableFlag = true;
    /**
     * 逻辑删除标记
     */
    private Boolean delFlag = false;

    public AbstractAuditingEntity() {
        super();
        this.enableFlag = true;
    }

    public Boolean getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    @LogicDelete(value = 1)
    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }
}
