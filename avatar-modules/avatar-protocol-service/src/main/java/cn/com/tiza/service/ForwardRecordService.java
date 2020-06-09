package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.TaskDataDao;
import cn.com.tiza.domain.TaskData;
import cn.com.tiza.domain.VehicleData;
import cn.com.tiza.service.dto.TaskQuery;
import cn.com.tiza.service.dto.VehicleQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author villas
 * @since 2019/5/30 14:45
 */
@SuppressWarnings("unchecked")
@Service
public class ForwardRecordService {

    @Autowired
    private TaskDataDao taskDataDao;

    public PageQuery<TaskData> findAll(TaskQuery query) {
        query.setOrganizationId(BaseContextHandler.getOrgId());
        PageQuery<TaskData> pageQuery = query.toPageQuery();
        taskDataDao.queryAll(pageQuery);
        return pageQuery;
    }

    public PageQuery queryVehicles(Long taskId, VehicleQuery query) {
        query.setTaskId(taskId);
        query.setOrganizationId(BaseContextHandler.getOrgId());
        PageQuery pageQuery = query.toPageQuery();
        taskDataDao.queryVehicleData(pageQuery);
        List<VehicleData> list = pageQuery.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(vehicleData -> {
                vehicleData.setFaiAmount(nvl(vehicleData.getReplyFaiAmount()) + nvl(vehicleData.getTimeoutFaiAmount()));
                vehicleData.setResendAmount(nvl(vehicleData.getPlatResendAmount()) + nvl(vehicleData.getTerminalResendAmount()));
            });
        }
        return pageQuery;
    }

    public PageQuery queryNotRelatedVehicles(Long taskId, VehicleQuery query) {
        query.setTaskId(taskId);
        query.setOrganizationId(BaseContextHandler.getOrgId());
        PageQuery<VehicleData> pageQuery = query.toPageQuery();
        taskDataDao.queryNotRelatedVehicles(pageQuery);
        return pageQuery;
    }

    private static Long nvl(Long val) {
        return Optional.ofNullable(val).orElse(0L);
    }
}
