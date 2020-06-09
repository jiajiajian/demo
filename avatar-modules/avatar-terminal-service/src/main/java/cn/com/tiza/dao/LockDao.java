package cn.com.tiza.dao;

import cn.com.tiza.domain.Lock;
import cn.com.tiza.web.rest.dto.VehicleVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
* gen by beetlsql mapper 2020-03-31
*/
public interface LockDao extends BaseMapper<Lock> {

    /**
     * 远程锁车分页查询
     * @param pageQuery
     */
    void pageQuery(PageQuery<Lock> pageQuery);

    /**
     * 控制车分页查询
     * @param pageQuery
     */
    List<VehicleVM> getVehicleList(PageQuery<VehicleVM> pageQuery);


    List<Lock> getVehicleLockList(@Param("vin") String vin);

    List<Lock> getLockFunctionSet(@Param("terminalCodeList") List<String> ters);
}
