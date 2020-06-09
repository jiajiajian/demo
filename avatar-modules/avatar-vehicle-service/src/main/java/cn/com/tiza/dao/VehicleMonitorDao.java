package cn.com.tiza.dao;

import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.service.dto.VehicleMonitorQuery;
import cn.com.tiza.web.rest.vm.FunctionSetItemVM;
import cn.com.tiza.web.rest.vm.VehicleLockItemVM;
import cn.com.tiza.web.rest.vm.VehicleMonitorVM;
import cn.com.tiza.web.rest.vm.VehicleVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author tiza
 */
public interface VehicleMonitorDao extends BaseMapper<Vehicle> {
    /**
     * 分页查询
     * @param pageQuery 分页条件
     */
    void getVehicleMonitorList(PageQuery<VehicleMonitorVM> pageQuery);

    void getVehicleMonitorOtherList(PageQuery<VehicleMonitorVM> pageQuery);

    /**
     * 根据VIN获取终端协议
     * @param vin vin码
     * @return 终端协议代码
     */
    String getProtocolByVin(@Param("vin") String vin);

    /**
     * 根据vin获取功能集详细配置
     * @param vin Vin码
     * @return 功能集列表
     */
    List<FunctionSetItemVM> getItemListByVin(@Param("vin") String vin);

    /**
     * 根据vin和code获取功能集详细配置
     * @param vin Vin码
     * @param code 代码
     * @return 功能集列表
     */
    List<FunctionSetItemVM> getItemListByVin(@Param("vin") String vin,@Param("code") String code);

    /**
     * 车辆锁车配置信息
     *
     * @param query
     */
    List<VehicleLockItemVM> getVehicleLockList(VehicleMonitorQuery query);

    /**
     * 获取车辆基本信息
     * @param id
     * @return
     */
    VehicleVM getVehicleBaseInfo(@Param("id") Long id);
}
