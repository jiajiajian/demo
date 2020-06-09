package cn.com.tiza.dao;

import cn.com.tiza.domain.MaintenanceBind;
import cn.com.tiza.service.dto.DataAnalyseQuery;
import cn.com.tiza.web.rest.vm.*;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

public interface DataAnalyseDao extends BaseMapper<MaintenanceBind> {
    /**
     * 区域车辆统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<VehicleAnalyseVM> getCountByProvince(DataAnalyseQuery dataAnalyseQuery);

    /**
     * 区域车辆统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<VehicleAnalyseVM> getCountByCity(DataAnalyseQuery dataAnalyseQuery);

    /**
     * 机型统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<VehicleAnalyseVM> getCountByVehicleType(DataAnalyseQuery dataAnalyseQuery);

    /**
     * 总工作小时统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<VehicleAnalyseVM> getTotalWorkTimeByProvince(DataAnalyseQuery dataAnalyseQuery);
    /**
     * 总工作小时统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<VehicleAnalyseVM> getTotalWorkTimeByCity(DataAnalyseQuery dataAnalyseQuery);

    /**
     * 工作时间统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<WorkTimeVM> getWorkTime(DataAnalyseQuery dataAnalyseQuery);

    /**
     * 开机明细统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<MachineWorkDetailVM> getMachineWorkDetail(DataAnalyseQuery dataAnalyseQuery);
    /**
     * 工作时间明细
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<WorkTimeDetailVM> getWorkTimeDetail(DataAnalyseQuery dataAnalyseQuery);

    /**
     * 在线率统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<OnlineRateVM> getOnlineRate(DataAnalyseQuery dataAnalyseQuery);

    /**
     * 工作总时间明细
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    List<WorkTimeDetailVM> getTotalWorkTimeDetail(DataAnalyseQuery dataAnalyseQuery);
}
