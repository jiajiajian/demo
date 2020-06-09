package cn.com.tiza.dao;

import cn.com.tiza.domain.FinanceOrganization;
import org.beetl.sql.core.mapper.BaseMapper;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * gen by beetlsql mapper 2020-03-06
 */
public interface FinanceOrganizationDao extends BaseMapper<FinanceOrganization> {

    /**
     * 计算融资机构数
     *
     * @param orgId
     * @return
     */
    default long countByOrgId(@NotNull Long orgId) {
        return createLambdaQuery()
                .andEq(FinanceOrganization::getOrgId, orgId)
                .count();
    }

    /**
     * delete
     * @param financeId f id
     * @return num
     */
    default int deleteByFinanceId(@NotNull Long financeId) {
        return createLambdaQuery()
                .andEq(FinanceOrganization::getFinanceId, financeId)
                .delete();
    }

    /**
     * select orgIds
     * @param financeId f id
     * @return num
     */
    default List<Long> getOrgIdsByFinanceId(@NotNull Long financeId) {
        return createLambdaQuery()
                .andEq(FinanceOrganization::getFinanceId, financeId)
                .select()
                .stream()
                .map(FinanceOrganization::getOrgId)
                .collect(Collectors.toList());
    }
}
