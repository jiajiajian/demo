package cn.com.tiza.dao;
import cn.com.tiza.domain.VehicleMaintenance;
import cn.com.tiza.web.rest.vm.VehicleMaintenanceVM;
import org.beetl.sql.core.annotatoin.*;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import cn.com.tiza.web.rest.*;

/**
* @author gen by beetlsql mapper 2020-03-31
*/
public interface VehicleMaintenanceDao extends BaseMapper<VehicleMaintenance> {
    /**
     * 分页查询
     *
     * @param pageQuery 分页条件
     */
    void pageQuery(PageQuery<VehicleMaintenanceVM> pageQuery);
}
