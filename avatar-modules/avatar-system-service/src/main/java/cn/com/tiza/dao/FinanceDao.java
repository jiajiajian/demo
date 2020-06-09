package cn.com.tiza.dao;

import cn.com.tiza.domain.Finance;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.web.rest.vm.FinanceVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author tiza
 */
public interface FinanceDao extends BaseMapper<Finance> {
    /**
     * 分页查询
     *
     * @param query 查询条件
     */
    void pageQuery(PageQuery<FinanceVM> query);

    /**
     * 导出查询
     *
     * @param paras 查询条件
     * @return list
     */
    List<FinanceVM> exportQuery(Map<String, Object> paras);

    /**
     * find by name
     *
     * @param name name
     * @return finance obj
     */
    default Optional<Finance> findByName(String name) {
        return Optional.ofNullable(createLambdaQuery()
                .andEq(Finance::getName, name)
                .single());
    }

    /**
     * find options by organizationId
     *
     * @param organizationId
     * @return
     */
    List<SelectOption> findOptionsByOrgId(@Param("organizationId") Long organizationId);
}
