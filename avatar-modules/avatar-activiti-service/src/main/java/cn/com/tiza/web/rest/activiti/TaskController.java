package cn.com.tiza.web.rest.activiti;

import cn.com.tiza.constant.ApplyStateEnum;
import cn.com.tiza.dao.LockApplyDao;
import cn.com.tiza.service.MyTaskService;
import cn.com.tiza.service.dto.DealDto;
import cn.com.tiza.service.dto.MyTaskQuery;
import cn.com.tiza.web.rest.vm.TaskVM;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * task manage
 *
 * @author villas
 */
@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController extends BaseController {

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private LockApplyDao lockApplyDao;

    @GetMapping("/candidate/tasks")
    @ApiOperation(value = "根据userId获取待办任务", notes = "获取待办任务")
    public ResponseEntity<List<TaskVM>> candidateTasks(MyTaskQuery query){
        List<TaskVM> tasks =  myTaskService.candidateTasks(query);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/revoke/{instanceId}")
    @ApiOperation(value = "根据流程实例id撤回至上一节点", notes = "任务撤回")
    public ResponseEntity revoke(@PathVariable("instanceId") String instanceId,@RequestBody DealDto dealDto){
        myTaskService.revoke(instanceId,dealDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/done/tasks")
    @ApiOperation(value = "获取我的已办任务列表", notes = "获取我的已办任务列表")
    public ResponseEntity<List<TaskVM>> doneTasks(MyTaskQuery query){
        List<TaskVM> tasks =  myTaskService.doneTasks(query);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/completeTask/{taskId}")
    @ApiOperation(value = "完成任务", notes = "完成任务，任务进入下一个节点")
    public ResponseEntity completeTask(@PathVariable String taskId, @RequestBody DealDto dealDto) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.addComment(taskId, task.getProcessInstanceId(),dealDto.getComment());
        taskService.setVariableLocal(taskId,"action",dealDto.getAction());
        Map<String,Object> vars = new HashMap<>();
        vars.put("action",dealDto.getAction());
        taskService.complete(taskId,vars);
        //只要有人审批就是在审批中
        lockApplyDao.updateStateByInstanceId(task.getProcessInstanceId(), ApplyStateEnum.approving.getState());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/turnTask/{taskId}/{userKey}")
    @ApiOperation(value = "任务转办", notes = "任务转办，把任务交给别人处理")
    public ResponseEntity turnTask(@PathVariable String taskId, @PathVariable String userKey) {
        taskService.setAssignee(taskId, userKey);
        return ResponseEntity.ok().build();
    }
}
