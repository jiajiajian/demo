package cn.com.tiza.dao;
import cn.com.tiza.domain.MaintenanceInfo;
import cn.com.tiza.domain.MaintenanceLog;
import cn.com.tiza.service.dto.MaintenanceLogQuery;
import cn.com.tiza.web.rest.vm.MaintenanceContentVM;
import cn.com.tiza.web.rest.vm.MaintenanceLogVM;
import cn.com.tiza.web.rest.vm.VehicleInfoVM;
import org.beetl.sql.core.annotatoin.*;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import cn.com.tiza.web.rest.*;

import java.util.List;

/**
* gen by beetlsql mapper 2020-03-31
 * @author tiza
*/
public interface MaintenanceLogDao extends BaseMapper<MaintenanceLog> {
    /**
     * 分页查询保养日志
     * @param query 查询条件
     */
    void pageQuery(PageQuery<MaintenanceLogVM> query);


    /**
     * 获取未保养数量
     * @return int
     */
    Integer count();

    /**
     * 获取所有车辆的信息
     * @return 车辆信息列表
     */
    List<VehicleInfoVM> getVehicleInfo();

    /**
     * 获取车辆保养项详细信息
     * @param vehicleTypeId 车辆类型ID
     * @param vehicleModelId 车辆型号ID
     * @return 车辆保养项详细信息列表
     */
    List<MaintenanceContentVM> getMaintenanceInfo(@Param("vehicleTypeId") long vehicleTypeId,@Param("vehicleModelId") long vehicleModelId);

    /**
     * 根据条件获取保养信息
     * @param tacticsId 策略ID
     * @param maintenanceType 保养指标
     * @param maintenanceContent 保养条目
     * @return 保养信息列表
     */
    List<MaintenanceInfo> getMaintenanceInfoByInfo(@Param("tacticsId") long tacticsId,@Param("maintenanceType") Integer maintenanceType,@Param("maintenanceContent") String maintenanceContent);

    /**
     * 根据条件获取保养记录
     * @param vin Vin码
     * @param maintenanceType 保养指标
     * @param maintenanceContent 保养条目
     * @return 保养记录列表
     */
    List<MaintenanceLog> getLogList(@Param("vin") String vin,@Param("maintenanceType") Integer maintenanceType,@Param("maintenanceContent") String maintenanceContent);
}
