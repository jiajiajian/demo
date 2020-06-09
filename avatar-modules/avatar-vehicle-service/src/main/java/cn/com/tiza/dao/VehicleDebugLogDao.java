package cn.com.tiza.dao;
import cn.com.tiza.domain.VehicleDebugLog;
import cn.com.tiza.web.rest.vm.VehicleDebugLogVM;
import org.beetl.sql.core.annotatoin.*;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import cn.com.tiza.web.rest.*;

/**
* gen
 * by beetlsql mapper 2020-03-17
*/
public interface VehicleDebugLogDao extends BaseMapper<VehicleDebugLog> {
    /**
     * 分页查询
     *
     * @param pageQuery 分页条件
     */
    void pageQuery(PageQuery<VehicleDebugLogVM> pageQuery);
}
