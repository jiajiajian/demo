package cn.com.tiza.service;

import cn.com.tiza.api.GrampusApiService;
import cn.com.tiza.dao.TaskDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author villas
 */
@Component
public class GrampusApiServiceImpl implements GrampusApiService {

    @Autowired
    private TaskDataDao taskDataDao;

    @Override
    public String getApiKey(String taskId) {
        return taskDataDao.queryApiKey(Long.parseLong(taskId));
    }
}
