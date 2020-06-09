package cn.com.tiza.dao;

import cn.com.tiza.domain.Charge;
import cn.com.tiza.web.rest.vm.AfterPaidVM;
import cn.com.tiza.web.rest.vm.ChargeVM;
import cn.com.tiza.web.rest.vm.PrePaidVM;
import cn.com.tiza.web.rest.vm.ServiceExpireWarnVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * gen by beetlsql mapper 2020-04-20
 */
public interface ChargeDao extends BaseMapper<Charge> {
    /**
     * 分页
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<ChargeVM> pageQuery);

    /**
     * find charge by orgRootId and terminalModel
     *
     * @param rootId
     * @param terminalModel
     * @return
     */
    default Charge findByOrgAndTerminalModel(Long rootId, String terminalModel) {
        return createLambdaQuery()
                .andEq(Charge::getOrganizationId, rootId)
                .andEq(Charge::getTerminalModel, terminalModel)
                .single();
    }

    /**
     * 分页
     *
     * @param pageQuery
     */
    void pageQueryPrePaid(PageQuery<PrePaidVM> pageQuery);

    /**
     * 后付费列表
     *
     * @param pageQuery
     */
    void pageQueryAfterPaid(PageQuery<AfterPaidVM> pageQuery);

    /**
     * 服务到期提醒
     *
     * @param pageQuery
     */
    void pageQueryServiceWarn(PageQuery<ServiceExpireWarnVM> pageQuery);

    /**
     * 首页提醒下月服务到期车辆数
     *@param organizationId
     *@param beginDate
     *@param endDate
     * @return
     */
    Integer warnCount(@Param("organizationId") Long organizationId, @Param("beginDate") Integer beginDate,
                   @Param("endDate") Integer endDate);
}
