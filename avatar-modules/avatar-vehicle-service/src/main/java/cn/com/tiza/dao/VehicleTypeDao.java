package cn.com.tiza.dao;

import cn.com.tiza.domain.VehicleType;
import cn.com.tiza.web.rest.vm.OptionVM;
import cn.com.tiza.web.rest.vm.VehicleTypeVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author tz0920
 */
public interface VehicleTypeDao extends BaseMapper<VehicleType> {
    /**
     * 分页查询
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<VehicleTypeVM> pageQuery);

    /**
     * 根据name查询本机构所属一级机构的车辆类型
     *
     * @param orgId
     * @param name
     * @return
     */
    VehicleType findByNameAndOrgId(@Param("orgId") Long orgId, @Param("name") String name);

    /**
     * 根据 根组织查询车辆类型下拉选
     *
     * @param orgId
     * @return
     */
    List<OptionVM> vehicleTypeOptionsByOrg(@Param("orgId") Long orgId);

    /**
     * 根据选择的组织查询 下拉选
     * @param orgId
     * @return
     */
    List<OptionVM> optionsBySelectOrg(@Param("orgId") Long orgId);

    /**
     * 根据融资机构 下拉选
     * @param financeId
     * @return
     */
    List<OptionVM> optionsByFinanceId(@Param("financeId") Long financeId);
}
