package cn.com.tiza.dao;

import cn.com.tiza.domain.VehicleRealtime;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * gen by beetlsql mapper 2020-03-10
 */
public interface VehicleRealtimeDao extends BaseMapper<VehicleRealtime> {
    /**
     * 更新故障状态
     *
     * @param vin
     * @param status
     */
    void updateFaultStatus(@Param("vin") String vin, @Param("status") Integer status);
}
