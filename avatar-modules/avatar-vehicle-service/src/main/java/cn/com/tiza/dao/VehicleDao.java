package cn.com.tiza.dao;

import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.service.dto.AvgModelOilConsumptionQuery;
import cn.com.tiza.service.dto.AvgOilConsumptionQuery;
import cn.com.tiza.service.dto.TonnageMonthVehicleNumQuery;
import cn.com.tiza.service.dto.VehicleQuery;
import cn.com.tiza.web.rest.vm.*;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-03-10
 */
public interface VehicleDao extends BaseMapper<Vehicle> {
    /**
     * 分页
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<VehicleVM> pageQuery);

    /**
     * 批量查询
     *
     * @param vinList
     */
    List<VehicleVM> batchQuery(@Param("vinList") List<String> vinList);

    /**
     * 车辆锁车配置信息
     *
     * @param query
     */
    List<VehicleLockItemVM> vehicleLockList(VehicleQuery query);

    /**
     * 查询和该车型关联车辆数量
     *
     * @param vehicleModelId
     * @return
     */
    default long countByVehicleModel(Long vehicleModelId) {
        return createLambdaQuery()
                .andEq(Vehicle::getVehicleModelId, vehicleModelId)
                .count();
    }

    /**
     * 根据vin 查询终端相关信息
     *
     * @param vin
     * @return
     */
    TerminalInfoVM findTerminalInfoByVin(@Param("vin") String vin);

    /**
     * 根据终端id查询终端信息
     *
     * @param id
     * @return
     */
    TerminalInfoVM findTerminalById(@Param("id") Long id);

    /**
     * 根据终端编号查询终端信息
     *
     * @param code
     * @return
     */
    TerminalInfoVM findTerminalByCode(@Param("code") String code);

    /**
     * 查询终端协议类型
     *
     * @param vin
     * @return
     */
    String terminalTypeByVin(@Param("vin") String vin);

    /**
     * find vinList by organizationId or financeId
     *
     * @param organizationId
     * @param financeId
     * @return
     */
    List<String> vinListByOrgIdOrFinanceId(@Param("organizationId") Long organizationId,
                                           @Param("financeId") Long financeId);

    /**
     * find vehicleList by vinList
     *
     * @param vinList
     * @return
     */
    List<Vehicle> vehicleListByVinList(@Param("vinList") List<String> vinList);

    /**
     * find orgList by organizationId
     *
     * @param organizationId
     * @return
     */
    List<OrganizationVM> findOrgList(@Param("organizationId") Long organizationId);

    /**
     * find vehicleList by organizationId
     *
     * @param organizationId
     * @return
     */
    List<VehicleTerminalVM> vehicleTerminalListByOrgId(@Param("organizationId") Long organizationId);

    /**
     * find all vin
     *
     * @return
     */
    List<String> findAllVin();

    /**
     * find vehicle by terminalCode
     *
     * @param terminalCode
     * @return
     */
    Vehicle findVehicleByTerminalCode(@Param("terminalCode") String terminalCode);

    /**
     * find vehicle and terminal info by vin
     *
     * @param vin
     * @return
     */
    VehicleTerminalVM vehicleAndTerminalInfoByVin(@Param("vin") String vin);

    /**
     * find vehicle base info by vin
     *
     * @param vin
     * @return
     */
    VehicleBaseInfoVM baseInfoByVin(@Param("vin") String vin);

    /**
     * 获取调试时间
     *
     * @param vin
     * @return
     */
    DebugDateVM getDebugDate(@Param("vin") String vin);

    /**
     * 单车弹框车辆基础信息
     *
     * @param vin
     * @return
     */
    VehicleVM monitorVehicleInfoByVin(@Param("vin") String vin);

    /**
     * find vehicle lock info by vin
     *
     * @param vin
     * @return
     */
    List<VehicleLockItemVM> vehicleLockInfoByVin(@Param("vin") String vin);

    /**
     * find frame vehicle info by vin
     *
     * @param vin
     * @return
     */
    FrameVehicleVM frameVehicleInfo(@Param("vin") String vin);

    /**
     * find rootId by orgid
     *
     * @param orgId
     * @return rootId
     */
    Long findRootOrgByOrg(@Param("orgId") Long orgId);

    /**
     * check vin permission for current org user
     *
     * @param orgId
     * @param vin
     * @return
     */
    String checkVinByOrgAndVin(@Param("orgId") Long orgId, @Param("vin") String vin);

    /**
     * @param vinList
     * @return
     */
    List<VehicleTerminalVM> vehicleAndTerminalInfoListByVinList(@Param("vinList") List<String> vinList);

    /**
     * find vehicle by vin or terminal_code or sim_code
     *
     * @param code
     * @return
     */
    Vehicle findVehicleByCode(@Param("code") String code);

    /**
     * 获取车辆协议类型和apikey
     *
     * @param vin
     * @return
     */
    ProtocolVM findProtocolTypeAndApiKey(@Param("vin") String vin);

    /**
     * 获取车辆协议类型
     *
     * @param vin
     * @return
     */
    String findProtocolType(@Param("vin") String vin);

    /**
     * 查找车辆根组织
     *
     * @param vin
     * @return
     */
    Long rootOrgIdByVin(@Param("vin") String vin);

    /**
     * 车辆平均油耗
     *
     * @param query
     * @return
     */
    List<AvgOilConsumptionVM> avgOilConsumption(AvgOilConsumptionQuery query);

    /**
     * 机型平均油耗统计分析
     * @param query
     * @return
     */
    List<AvgModelOilConsumptionVM> avgModelOilConsumption(AvgModelOilConsumptionQuery query);

    /**
     * 吨位车辆数统计
     * @param query
     * @return
     */
    List<TonnageMonthGroupVM> monthTonnageVehicleNum(TonnageMonthVehicleNumQuery query);

    /**
     * 获取tla数量
     * @param rootOrgId
     * @return
     */
    Long tlaCount(@Param("rootOrgId") Long rootOrgId);

    default long vehicleCountByVin(String vin){
        return createLambdaQuery()
                .andEq(Vehicle::getVin, vin)
                .count();
    }
}
