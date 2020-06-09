package cn.com.tiza.service;

import cn.com.tiza.constant.ActionEnum;
import cn.com.tiza.constant.ProcDefKey;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.LockApplyDao;
import cn.com.tiza.domain.LockApply;
import cn.com.tiza.service.dto.DealDto;
import cn.com.tiza.service.dto.MyTaskQuery;
import cn.com.tiza.web.rest.AccountApiClient;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.vm.TaskVM;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author villas
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MyTaskService {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private LockApplyDao lockApplyDao;

    @Autowired
    private ProcessService processService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AccountApiClient accountApiClient;

    /**
     * 启动流程
     *
     * @param procDefKey 流程定义KEY
     * @param businessId 业务表编号
     * @param title      流程标题，显示在待办任务标题
     * @param vars       流程变量
     * @param consumer   业务消费
     * @return 流程实例ID
     */
    public String startProcess(String procDefKey, String businessId, String title, Map<String, Object> vars,
                               Consumer<ProcessInstance> consumer) {
        Objects.requireNonNull(consumer);

        // 用来设置启动流程的人员ID
        identityService.setAuthenticatedUserId(BaseContextHandler.getUserID().toString());
        // 设置流程变量
        vars = Optional.ofNullable(vars).orElseGet(HashMap::new);
        // 设置流程标题
        if (StringUtils.isNotEmpty(title)) {
            vars.put("title", title);
        }

        vars.put("role_name","");
        List<String> roleName = accountApiClient.findUserRoleName(BaseContextHandler.getUserID());
        if(Objects.nonNull(roleName) && roleName.size() == 1){
            vars.put("role_name",roleName.get(0));
        }
        // 启动流程
        ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessId, vars);

        // 执行业务操作
        consumer.accept(procIns);
        return procIns.getId();
    }

    public List<TaskVM> candidateTasks(MyTaskQuery query) {

        String userId = query.getUserId();

        List<Task> tasks = new ArrayList<>();

        // 根据当前人的ID查询
        List<Task> todoList = taskService.createTaskQuery().processDefinitionKey(ProcDefKey.CONTROLLER_LOCK).taskAssignee(userId).list();

        // 根据当前人未签收的任务
        List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey(ProcDefKey.CONTROLLER_LOCK).taskCandidateUser(userId).list();

        // 合并
        tasks.addAll(todoList);
        tasks.addAll(unsignedTasks);

        return tasks.stream().map(this::convertToDo).collect(Collectors.toList());
    }

    public List<TaskVM> doneTasks(MyTaskQuery query){
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(ProcDefKey.CONTROLLER_LOCK)
                .taskAssignee(query.getUserId())
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        return tasks.stream().map(this::convertDone).collect(Collectors.toList());
    }

    private TaskVM convertToDo(Task task){
        TaskVM vm = new TaskVM();

        vm.setTaskId(task.getId());
        vm.setTaskName(task.getName());
        String businessKey = processService.findBusinessKeyByInstanceId(task.getProcessInstanceId());
        vm.setInstanceId(task.getProcessInstanceId());
        LockApply lockApply = lockApplyDao.getApplyByCode(businessKey);
        if(Objects.nonNull(lockApply)){
            vm.setApplyCode(lockApply.getApplyCode());
            vm.setApplyUserName(lockApply.getApplyUser());
            vm.setApplyTime(lockApply.getCreateTime());
            vm.setReason(lockApply.getReason());
        }
        return vm;
    }

    private TaskVM convertDone(HistoricTaskInstance task){
        TaskVM vm = new TaskVM();
        String businessKey = processService.findBusinessKeyByInstanceId(task.getProcessInstanceId());
        LockApply lockApply = lockApplyDao.getApplyByCode(businessKey);
        vm.setTaskId(task.getId());
        vm.setTaskName(task.getName());
        vm.setApplyCode(lockApply.getApplyCode());
        vm.setApplyUserName(lockApply.getApplyUser());
        vm.setApplyTime(lockApply.getCreateTime());
        vm.setReason(lockApply.getReason());
        vm.setInstanceId(lockApply.getInstanceId());
        vm.setDoneTime(task.getEndTime());
        //获取处理动作
        List<HistoricVariableInstance> actionList = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).list();
        if(Objects.nonNull(actionList)){
           for (HistoricVariableInstance var : actionList){
               if("action".equals(var.getVariableName())){
                   vm.setAction(ActionEnum.getType((Integer) var.getValue()));
               }
           }
        }
        return vm;
    }

    public void revoke(String instanceId, DealDto dealDto) {
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        if(task==null) {
            //流程未启动或已执行完成，无法撤回
            throw new BadRequestException("error.activiti.revoke.exist.or.complete");
        }
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(instanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        for(HistoricTaskInstance hti : htiList) {
            if(BaseContextHandler.getUserID().toString().equals(hti.getAssignee())) {
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if(null==myTaskId) {
            //该任务非当前用户提交，无法撤回
            throw new BadRequestException("error.activiti.revoke.not.current.user");
        }

        String processDefinitionId = myTask.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        String myActivityId = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(myTask.getExecutionId()).finished().orderByHistoricActivityInstanceStartTime().desc().list();
        for(HistoricActivityInstance hai : haiList) {
            if(myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);
        if(Objects.isNull(myFlowNode)){
            //没有回退的节点
            throw new BadRequestException("error.activiti.revoke.no.target");
        }
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        log.warn("------->> activityId:" + activityId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

        //清理活动方向
        flowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(myFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);

        Authentication.setAuthenticatedUserId(BaseContextHandler.getUserID().toString());
        taskService.addComment(task.getId(), task.getProcessInstanceId(), dealDto.getComment());
        Map<String,Object> currentVariables = new HashMap<String,Object>();
        currentVariables.put("applier", BaseContextHandler.getLoginName());
        currentVariables.put("action",dealDto.getAction());
        //完成任务
        taskService.complete(task.getId(),currentVariables);
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);
    }
}
