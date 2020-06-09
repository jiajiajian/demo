package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.TaskDao;
import cn.com.tiza.fwp.FwpApiClient;
import cn.com.tiza.service.dto.TaskQuery;
import cn.com.tiza.vm.ForwardJobVM;
import cn.com.tiza.vm.TaskDto;
import cn.com.tiza.vm.TaskVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author villas
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private FwpApiClient fwpApiClient;

    private static final int FIVE_MINUTE = 300_000;

    private static final String START_FAILED = "start_failed";
    private static final String RUNNING_FAILED = "running_failed";
    private static final String RUNNING = "running";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @SuppressWarnings("unchecked")
    public PageQuery<TaskVM> findAll(TaskQuery query) {
        query.setOrganizationId(BaseContextHandler.getOrgId());
        PageQuery<TaskVM> pageQuery = query.toPageQuery();
        taskDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<TaskVM> get(Long id) {
        return Optional.ofNullable(fwpApiClient.getTask(id));
    }

    public TaskVM create(TaskDto command) {
        command.setOrganizationId(BaseContextHandler.getOrgId());
        return fwpApiClient.createTask(command);
    }

    public void update(Long id, TaskDto command) {
        fwpApiClient.updateTask(id, command);
    }

    public void delete(Long[] ids) {
        fwpApiClient.deleteTask(ids);
    }

    public List<ForwardJobVM> queryJobs(Long taskId) {
        ForwardJobVM[] jobs = fwpApiClient.queryJobs(taskId);
        return Arrays.asList(jobs);
    }

    public Integer bind(Long taskId, List<String> vinList) {
        return fwpApiClient.taskAddVehicles(taskId, vinList);
    }

    public void unbind(Long taskId, String[] vinList) {
        fwpApiClient.deleteVehicle(taskId, vinList);
    }

    public void run(Long taskId) {
        fwpApiClient.runTask(taskId);
    }

    public void stop(Long taskId) {
        fwpApiClient.stopTask(taskId);
    }

    public Map<String, String> queryTaskStatus(Long taskId) {
        Map<String, String> result = new LinkedHashMap<>();
        HashOperations<String, String, Object> opsForHash = stringRedisTemplate.opsForHash();
        Map<String, Object> entries = opsForHash.entries("FWP_TASK_" + taskId);
        entries.forEach((k, v) -> result.put(k, this.checkStatus(v)));
        long count = result.values().stream().filter(RUNNING_FAILED::equals).count();
        if (count > 0) {
            result.put("status", RUNNING_FAILED);
        }
        return result;
    }

    private String checkStatus(Object value) {
        if (Objects.isNull(value)) {
            return START_FAILED;
        }
        long time = Long.parseLong(value.toString());
        if (time == -1L) {
            return START_FAILED;
        }
        long duration = System.currentTimeMillis() - FIVE_MINUTE;
        return duration > time ? RUNNING_FAILED : RUNNING;
    }

    public List<String> findBoundVinByTask(Long taskId) {
        return taskDao.findBoundVinByTask(taskId);
    }

    public List<String> queryAllVin() {
        return taskDao.queryAllVin(BaseContextHandler.getOrgId());
    }

}
