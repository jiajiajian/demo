package cn.com.tiza.dao;

import cn.com.tiza.domain.VehicleModel;
import cn.com.tiza.web.rest.vm.OptionVM;
import cn.com.tiza.web.rest.vm.VehicleModelVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * gen by beetlsql mapper 2020-03-10
 */
public interface VehicleModelDao extends BaseMapper<VehicleModel> {
    /**
     * 分页查询
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<VehicleModelVM> pageQuery);

    /**
     * 根据车辆类型查询关联车辆型号数量
     *
     * @param vehicleTypeId
     * @return
     */
    default long countByVehicleType(Long vehicleTypeId) {
        return createLambdaQuery()
                .andEq(VehicleModel::getVehicleTypeId, vehicleTypeId)
                .count();
    }

    /**
     * 根据name查询本机构所属一级机构的车辆型号
     *
     * @param orgId
     * @param name
     * @return
     */
    VehicleModel findByNameAndOrgId(@Param("orgId") Long orgId, @Param("name") String name);

    /**
     * 根据组织查询车型下拉选
     *
     * @param orgId
     * @return
     */
    List<OptionVM> vehicleModelOptionsByOrg(@Param("orgId") Long orgId);

    /**
     * 根据融资机构查询车型下拉选
     *
     * @param financeId
     * @return
     */
    List<OptionVM> optionsByFinanceId(@Param("financeId") Long financeId);

    /**
     * 根据车辆类型 查询车型下拉选
     *
     * @param vehicleType
     * @return
     */
    default List<OptionVM> vehicleModelOptionsVehicleType(Long vehicleType) {
        return createLambdaQuery()
                .andEq(VehicleModel::getVehicleTypeId, vehicleType)
                .select("ID", "NAME")
                .stream().map(model -> {
                    OptionVM optionVM = new OptionVM();
                    optionVM.setId(model.getId());
                    optionVM.setName(model.getName());
                    return optionVM;
                }).collect(Collectors.toList());
    }
}
