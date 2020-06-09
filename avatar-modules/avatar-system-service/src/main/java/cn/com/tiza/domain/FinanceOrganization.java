package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Data
@Table(name = "base_finance_organization")
public class FinanceOrganization implements Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 融资机构ID
     */
    private Long financeId;
    /**
     * 机构ID
     */
    private Long orgId;
    /**
     * 创建时间
     */
    private Long createTime;

    public FinanceOrganization() {
    }

    public FinanceOrganization(Long financeId, Long orgId) {
        this.financeId = financeId;
        this.orgId = orgId;
        this.createTime = System.currentTimeMillis();
    }
}
