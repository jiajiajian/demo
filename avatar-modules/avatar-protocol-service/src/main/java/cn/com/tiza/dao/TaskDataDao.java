package cn.com.tiza.dao;

import cn.com.tiza.domain.TaskData;
import cn.com.tiza.domain.VehicleData;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * @author villas
 */
public interface TaskDataDao extends BaseMapper<TaskData> {
    /**
     * 分页查询转发记录
     *
     * @param pageQuery 参数
     */
    void queryAll(PageQuery<TaskData> pageQuery);

    /**
     * 分页查询和任务绑定的车辆以及车辆相关的转发报文帧数
     *
     * @param pageQuery 参数
     */
    void queryVehicleData(PageQuery<VehicleData> pageQuery);

    /**
     * 分页查询未绑定转发任务的车辆
     *
     * @param pageQuery 参数
     */
    void queryNotRelatedVehicles(PageQuery<VehicleData> pageQuery);

    /**
     * get the apiKey by vin
     *
     * @param id task id
     * @return apiKey
     */
    String queryApiKey(@Param("id") Long id);
}
