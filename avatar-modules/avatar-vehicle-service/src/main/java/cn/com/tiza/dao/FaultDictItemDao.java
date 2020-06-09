package cn.com.tiza.dao;

import cn.com.tiza.domain.FaultDictItem;
import cn.com.tiza.web.rest.vm.FaultDictItemVM;
import cn.com.tiza.web.rest.vm.SpnFmiTlaVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-03-10
 */
public interface FaultDictItemDao extends BaseMapper<FaultDictItem> {
    /**
     * 分页
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<FaultDictItemVM> pageQuery);

    /**
     * findBySpnFmiTla
     *
     * @param spn
     * @param fmi
     * @param tlaId
     * @return
     */
    default FaultDictItem findBySpnFmiTla(String spn, String fmi, Long tlaId,Long organizationId) {
        return createLambdaQuery()
                .andEq(FaultDictItem::getSpn, spn)
                .andEq(FaultDictItem::getFmi, fmi)
                .andEq(FaultDictItem::getTlaId, tlaId)
                .andEq(FaultDictItem::getOrganizationId,organizationId)
                .single();
    }

    default FaultDictItem findBySpnFmi(String spn, String fmi, Long organizationId) {
        return createLambdaQuery()
                .andEq(FaultDictItem::getSpn, spn)
                .andEq(FaultDictItem::getFmi, fmi)
                .andEq(FaultDictItem::getOrganizationId,organizationId)
                .single();
    }

    /**
     * spnFmiTlaQueryByRootOrg
     *
     * @param rootOrgId
     * @return
     */
    List<SpnFmiTlaVM> spnFmiTlaQueryByRootOrg(@Param("rootOrgId") Long rootOrgId);

    /**
     * tlaList
     *
     * @param rootOrgId
     * @return
     */
    List<String> tlaList(@Param("rootOrgId") Long rootOrgId);

    /**
     * tlaId
     *
     * @param rootOrgId
     * @param tla
     * @return
     */
    Long tlaId(@Param("rootOrgId") Long rootOrgId, @Param("tla") String tla);

}
