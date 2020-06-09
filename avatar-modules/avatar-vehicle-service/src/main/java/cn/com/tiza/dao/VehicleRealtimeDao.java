package cn.com.tiza.dao;

import cn.com.tiza.domain.VehicleRealtime;
import cn.com.tiza.service.dto.WorkTimeDto;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-03-10
 */
public interface VehicleRealtimeDao extends BaseMapper<VehicleRealtime> {
    void pageQuery(PageQuery pageQuery);

    /**
     * 工作时间分析
     *
     * @param dto dto
     * @return realtime list
     */
    List<VehicleRealtime> workTimeStatistic(WorkTimeDto dto);
}
