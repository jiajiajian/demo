package cn.com.tiza.dao;

import cn.com.tiza.domain.Task;
import cn.com.tiza.vm.TaskVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * 
 * @author villas
 * 
 */
public interface TaskDao extends BaseMapper<Task> {
    /**
     * 转发配置的分页查询
     * 
     * @param pageQuery 分页参数
     */
    void pageQuery(PageQuery<TaskVM> pageQuery);


    /**
     * 获取该组织下的所有车辆
     *
     * @param orgId 组织id
     * @return 所有车辆的vin码
     */
    List<String> queryAllVin(@Param("orgId") Long orgId);

    /**
     * 根据指定的任务id获取已经绑定vin
     *
     * @param taskId 任务id
     * @return 绑定的车辆vin码集合
     */
    List<String> findBoundVinByTask(@Param("taskId") Long taskId);
}
