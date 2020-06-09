package cn.com.tiza.dao;

import cn.com.tiza.domain.BusinessLog;
import cn.com.tiza.web.rest.vm.BusinessLogVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-04-16
 */
public interface BusinessLogDao extends BaseMapper<BusinessLog> {
    void pageQuery(PageQuery<BusinessLogVM> pageQuery);

    List<BusinessLogVM> history(@Param("vin") String vin,@Param("operateType") Integer operateType);
}
