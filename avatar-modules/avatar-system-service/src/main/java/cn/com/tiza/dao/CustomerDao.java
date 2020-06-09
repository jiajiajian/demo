package cn.com.tiza.dao;

import cn.com.tiza.domain.Customer;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.web.rest.vm.CustomerVO;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author tiza
 */
public interface CustomerDao extends BaseMapper<Customer> {
    /**
     * 分页查询
     *
     * @param pageQuery paras
     */
    void pageQuery(PageQuery<CustomerVO> pageQuery);

    /**
     * 导出查询
     *
     * @param paras paras
     * @return list
     */
    List<CustomerVO> exportQuery(Map<String, Object> paras);

    /**
     * find by name
     *
     * @param organizationId organizationId
     * @param name name
     * @return Customer obj
     */
    default Optional<Customer> findByName(@NotNull Long organizationId, @NotNull String name) {
        return Optional.ofNullable(createLambdaQuery()
                .andEq(Customer::getOrganizationId, organizationId)
                .andEq(Customer::getName, name)
                .single());
    }

    /**
     * find by organizationId
     *
     * @param organizationId organizationId
     * @return Customer list
     */
    default List<SelectOption> optionsByOrgId(@NotNull Long organizationId) {
        return createLambdaQuery()
                .andEq(Customer::getOrganizationId, organizationId)
                .select()
                .stream()
                .map(c -> new SelectOption(c.getId(), null, c.getName()))
                .collect(Collectors.toList());
    }

    /**
     * count by org
     *
     * @param organizationId organizationId
     * @return count
     */
    default long countByOrgId(@NotNull Long organizationId) {
        return createLambdaQuery()
                .andEq(Customer::getOrganizationId, organizationId)
                .count();
    }
}
