package cn.com.tiza.service;

import cn.com.tiza.constant.ActionEnum;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.vm.HistoricActivityVM;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import static com.vip.vjtools.vjkit.time.DateFormatUtil.PATTERN_DEFAULT_ON_SECOND;
import static com.vip.vjtools.vjkit.time.DateFormatUtil.formatDate;

/**
 * @author villas
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    private static final String PNG = ".png";

    private static final int BYTE_SIZE = 2 << 9;

    public void readProcessResource(String instanceId, HttpServletResponse response) {
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        try{
            String processDefinitionId;
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(instanceId).singleResult();
            if (processInstance == null) {
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(instanceId).singleResult();
                processDefinitionId = historicProcessInstance.getProcessDefinitionId();
            } else {
                processDefinitionId = processInstance.getProcessDefinitionId();
            }
            ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
            ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

            String resourceName = pd.getDiagramResourceName();

            if (resourceName.endsWith(PNG) && StringUtils.isNotEmpty(instanceId)) {
                getActivitiProccessImage(instanceId, response);
            } else {
                // 通过接口读取
                InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);
                // 输出资源内容到相应对象
                byte[] b = new byte[BYTE_SIZE];
                int len = 0;
                while ((len = resourceAsStream.read(b, 0, BYTE_SIZE)) != -1) {
                    response.getOutputStream().write(b, 0, len);
                }
            }
        }catch (Exception ex){
            throw new BadRequestAlertException("processImg can not view",null,
                    "process.img.is.not.view");
        }
    }

    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     */
    private void getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response) {
        try {
            //  获取历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(pProcessInstanceId).singleResult();

            if (historicProcessInstance == null) {
            } else {
                // 获取流程定义
                ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

                // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
                List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(pProcessInstanceId).orderByHistoricActivityInstanceId().asc().list();

                // 已执行的节点ID集合
                List<String> executedActivityIdList = new ArrayList<>();
                for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                    executedActivityIdList.add(activityInstance.getActivityId());
                }

                BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

                // 已执行的线集合
                List<String> flowIds;
                // 获取流程走过的线 (getHighLightedFlows是下面的方法)
                flowIds = getHighLightedFlows(bpmnModel, historicActivityInstanceList);

                // 获取流程图图像字符流
                ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
                //配置字体
                InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds,
                        "宋体", "微软雅黑", "黑体", null, 2.0);

                response.setContentType("image/png");
                OutputStream os = response.getOutputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[BYTE_SIZE];
                while ((bytesRead = imageStream.read(buffer, 0, BYTE_SIZE)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                imageStream.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException("获取流程图失败！" + e.getMessage());
        }
    }

    private static String format(Date date) {
        return formatDate(PATTERN_DEFAULT_ON_SECOND, date);
    }

    public List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // 用以保存高亮的线flowId
        List<String> highFlows = new ArrayList<>();

        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 对历史流程节点进行遍历
            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());

            // 用以保存后续开始时间相同的节点
            List<FlowNode> sameStartTimeNodes = new ArrayList<>();
            FlowNode sameActivityImpl1 = null;
            // 第一个节点
            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);
            HistoricActivityInstance activityImp2_;

            for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
                // 后续第1个节点
                activityImp2_ = historicActivityInstances.get(k);

                //都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
                if (activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
                        format(activityImpl_.getStartTime()).equals(format(activityImp2_.getStartTime()))) {

                } else {
                    //找到紧跟在后面的一个节点
                    sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());
                    break;
                }

            }
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                // 后续第一个节点
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);
                // 后续第二个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);

                // 如果第一个节点和第二个节点开始时间相同保存
                if (format(activityImpl1.getStartTime()).equals(format(activityImpl2.getStartTime()))) {
                    FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
            // 取出节点的所有出去的线
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows();

            // 对所有的线进行遍历
            for (SequenceFlow pvmTransition : pvmTransitions) {
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }

        }
        return highFlows;

    }

    public List<HistoricActivityVM> selectHistoryList(String processInstanceId){
        List<HistoricActivityVM> activityList = new ArrayList<>();
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        List<HistoricActivityInstance> list = query.processInstanceId(processInstanceId)
                .activityType("userTask").finished().orderByHistoricActivityInstanceStartTime().desc().list();
        list.forEach(instance->{
            HistoricActivityVM activity = new HistoricActivityVM();
            BeanUtils.copyProperties(instance, activity);
            String taskId = instance.getTaskId();
            //备注
            List<Comment> comment = taskService.getTaskComments(taskId, "comment");
            if (!CollectionUtils.isEmpty(comment)) {
                activity.setComment(comment.get(0).getFullMessage());
            }
            //审批人
            if(Objects.nonNull(instance.getAssignee())){
                User sysUser =  identityService.createUserQuery().userId(instance.getAssignee()).singleResult();
                if (Objects.nonNull(sysUser)) {
                    activity.setAssigneeName(sysUser.getFirstName());
                }
            }
            //获取处理动作
            List<HistoricVariableInstance> actionList = historyService.createHistoricVariableInstanceQuery().taskId(taskId).list();
            if(Objects.nonNull(actionList)){
                for (HistoricVariableInstance var : actionList){
                    if("action".equals(var.getVariableName())){
                        activity.setAction(ActionEnum.getType((Integer) var.getValue()));
                    }
                }
            }
            activityList.add(activity);
        });
        return activityList;
    }

    /**
     * 挂起流程实例
     * @param instanceId
     * @param suspendState
     */
    public void suspendOrActiveApplyInstance(String instanceId, String suspendState) {
        if ("1".equals(suspendState)) {
            // 当流程实例被挂起时，无法通过下一个节点对应的任务id来继续这个流程实例。
            // 通过挂起某一特定的流程实例，可以终止当前的流程实例，而不影响到该流程定义的其他流程实例。
            // 激活之后可以继续该流程实例，不会对后续任务造成影响。
            // 直观变化：act_ru_task 的 SUSPENSION_STATE_ 为 2
            runtimeService.suspendProcessInstanceById(instanceId);
        } else if ("2".equals(suspendState)) {
            runtimeService.activateProcessInstanceById(instanceId);
        }
    }

    /**
     * 挂起流程定义
     * @param definitionId
     * @param suspendState
     */
    public void suspendOrActiveApplyDefinition(String definitionId, String suspendState) {
        if ("1".equals(suspendState)) {
            // 当流程定义被挂起时，已经发起的该流程定义的流程实例不受影响（如果选择级联挂起则流程实例也会被挂起）。
            // 当流程定义被挂起时，无法发起新的该流程定义的流程实例。
            // 直观变化：act_re_procdef 的 SUSPENSION_STATE_ 为 2
            repositoryService.suspendProcessDefinitionById(definitionId);
        } else if ("2".equals(suspendState)) {
            repositoryService.activateProcessDefinitionById(definitionId);
        }
    }

    public String findBusinessKeyByInstanceId(String instanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceId)
                    .singleResult();
            return historicProcessInstance.getBusinessKey();
        } else {
            return processInstance.getBusinessKey();
        }
    }

    public  List<Map<String, Object>> getMaps(List<ProcessDefinition> models) {
        List<Map<String, Object>> pd = new ArrayList<>();
        for(ProcessDefinition processDefinition:models) {
            ProcessDefinitionEntityImpl entityImpl = (ProcessDefinitionEntityImpl) processDefinition;
            Map<String, Object> values = new HashMap<String,Object>();
            values.put("id", processDefinition.getId());
            values.put("name", processDefinition.getName());
            values.put("key", processDefinition.getKey());
            values.put("version", processDefinition.getVersion());
            values.put("resourceName",processDefinition.getResourceName());
            values.put("diagramResourceName",processDefinition.getDiagramResourceName());
            values.put("category",processDefinition.getCategory());
            Deployment deployment = repositoryService.createDeploymentQuery()
                    .deploymentId(processDefinition.getDeploymentId())
                    .singleResult();
            values.put("deploymentId",processDefinition.getDeploymentId());
            values.put("deploymentTime",deployment.getDeploymentTime());
            values.put("suspensionState",entityImpl.getSuspensionState());
            pd.add(values);
        }
        return pd;
    }

    public void readDefinitionResource(String processDefinitionId, String resourceName, HttpServletResponse response) {
        try{
            ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
            org.activiti.engine.repository.ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();
            // 通过接口读取
            InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);
            // 输出资源内容到相应对象
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        }catch(Exception ex){
            throw new BadRequestAlertException("definition img can not view",null,
                    "definition.img.is.not.view");
        }
    }
}
