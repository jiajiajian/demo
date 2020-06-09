package cn.com.tiza.dao;

import cn.com.tiza.domain.ExportTaskVehicle;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * 
 * @author villas 2019-06-13
 */
public interface ExportTaskVehicleDao extends BaseMapper<ExportTaskVehicle> {

    /**
     * 根据任务id查询绑定的车辆VIN码集合
     * 
     * @param taskId 任务id
     * @return VIN码集合
     */
    List<String> queryVinListOfForward(@Param("taskId") Long taskId);

    /**
     * 查询出指定的任务下车辆的绑定时间
     * 
     * @param vin 车辆vin码
     * @param taskId 任务id
     * @return 绑定时间
     */
    Long queryCreateTime(@Param("vin") String vin, @Param("taskId") Long taskId);

    /**
     * 删除关联表数据
     * 
     * @param exportTaskId 导出任务id
     */
    void deleteByExportTaskId(@Param("exportTaskId") Long exportTaskId);
}
