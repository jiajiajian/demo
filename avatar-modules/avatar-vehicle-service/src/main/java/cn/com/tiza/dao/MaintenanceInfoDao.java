package cn.com.tiza.dao;
import cn.com.tiza.domain.MaintenanceInfo;
import cn.com.tiza.web.rest.vm.MaintenanceInfoVM;
import org.beetl.sql.core.annotatoin.*;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import cn.com.tiza.web.rest.*;

import java.util.List;

/**
* @author  gen by beetlsql mapper 2020-03-31
*/
public interface MaintenanceInfoDao extends BaseMapper<MaintenanceInfo> {
    /**
     * 根据策略ID获取已设置的保养信息
     * @param tacticsId 策略ID
     * @return 保养信息列表
     */
	List<MaintenanceInfoVM> getInfoListByTacticsId(@Param("tacticsId") Long tacticsId);

    /**
     * 根据保养记录ID获取保养信息
     * @param logId 保养记录ID
     * @return 保养信息列表
     */
	List<MaintenanceInfoVM> getInfoListByLogId(@Param("logId") Long logId);
}
