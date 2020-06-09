package cn.com.tiza.dao;

import cn.com.tiza.domain.CmdDebug;
import cn.com.tiza.domain.VehicleDebug;
import cn.com.tiza.domain.VehicleDebugLog;
import cn.com.tiza.service.dto.VehicleDebugQuery;
import cn.com.tiza.web.rest.vm.CmdDebugVM;
import cn.com.tiza.web.rest.vm.VehicleDebugVM;
import cn.com.tiza.web.rest.vm.VehicleLockItemVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

public interface VehicleDebugDao extends BaseMapper<VehicleDebug> {
    /**
     * 分页查询
     *
     * @param pageQuery 分页条件
     */
    void pageQuery(PageQuery<VehicleDebugVM> pageQuery);

    /**
     * 分页查询新增调试车列表（剔除已经在调试表中）
     * @param pageQuery 分页条件
     */
    void getVehicleList(PageQuery<VehicleDebugVM> pageQuery);

    /**
     * 分页查询调试日志
     *
     * @param pageQuery 分页条件
     */
    void getVehicleDebugLogList(PageQuery<VehicleDebugVM> pageQuery);

    /**
     * 根据ID查询信息
     * @param id 主键
     * @return 实体
     */
    VehicleDebugVM getVehicleDebugById(@Param("id") Long id);

    /**
     * 获取各个状态下的数量
     */
    List<VehicleDebugVM> getCountByStatus();

    /**
     * 根据车型和组织获取调试项
     * @param orgId 组织ID
     * @param vehicleModelId 车型ID
     * @return 实体
     */
    CmdDebug getCmdDebugByInfo(@Param("orgId") Long orgId, @Param("vehicleModelId") Long vehicleModelId);

    /**
     * 根据vin获取调试项信息
     * @param vin vin码
     * @return 调试项列表
     */
    List<VehicleDebugLog> getDebugLogList(@Param("vin") String vin);

    /**
     * 根据组织获取调试车的Vin码
     * @param organizationId 组织ID
     * @return Vin码列表
     */
    List<String> getVinListInDebug(@Param("organizationId") Long organizationId);

    /**
     * 车辆锁车配置信息
     * @param vehicleDebugQuery 查询条件
     * @return 信息
     */
    List<VehicleLockItemVM> vehicleLockList(VehicleDebugQuery vehicleDebugQuery);

    String getDebugIdByVehicleId(@Param("vehicleId") Long vehicleId);
}
