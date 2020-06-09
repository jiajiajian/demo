package cn.com.tiza.dao;
import cn.com.tiza.domain.MaintenanceBind;
import cn.com.tiza.domain.MaintenanceTactics;
import cn.com.tiza.web.rest.vm.MaintenanceTacticsVM;
import org.beetl.sql.core.annotatoin.*;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import cn.com.tiza.web.rest.*;
import org.hibernate.validator.constraints.SafeHtml;

import java.util.List;

/**
* @author gen by beetlsql mapper 2020-03-31
*/
public interface MaintenanceTacticsDao extends BaseMapper<MaintenanceTactics> {
    /**
     * 分页查询
     *
     * @param pageQuery 分页条件
     */
    void pageQuery(PageQuery<MaintenanceTactics> pageQuery);

    /**
     * 根据策略ID获取绑定的车型
     * @param tacticsId 策略ID
     * @return 策略绑定车型列表
     */
    List<MaintenanceBind> getBindListByTacticsId(@Param("tacticsId") Long tacticsId);
}
