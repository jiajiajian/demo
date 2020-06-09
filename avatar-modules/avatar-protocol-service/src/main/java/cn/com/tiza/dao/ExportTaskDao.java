package cn.com.tiza.dao;

import cn.com.tiza.domain.ExportTask;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;


/**
 * @author villas gen by beetlsql mapper 2019-06-13
 */
public interface ExportTaskDao extends BaseMapper<ExportTask> {

    /**
     * select all export's task which belong the specific org
     *
     * @param orgId the id of organazition
     * @return the list of export's task
     */
    List<ExportTask> findAll(@Param("orgId") Long orgId);
}
