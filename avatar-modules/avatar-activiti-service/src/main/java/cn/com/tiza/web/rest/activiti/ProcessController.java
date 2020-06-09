package cn.com.tiza.web.rest.activiti;

import cn.com.tiza.service.ProcessService;
import cn.com.tiza.service.dto.ProcessQuery;
import cn.com.tiza.web.rest.vm.HistoricActivityVM;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static cn.com.tiza.web.rest.activiti.ModelController.getFirstResult;

/**
 * @author villas
 */
@Slf4j
@RestController
@RequestMapping("/process")
public class ProcessController extends BaseController {

    @Autowired
    private ProcessService processService;

    @GetMapping("/listHistory/{instanceId}")
    @ApiOperation(value = "根据流程id查询流程审批历史", notes = "审批历史")
    public List<HistoricActivityVM> listHistory(@PathVariable("instanceId") String instanceId) {
        return processService.selectHistoryList(instanceId);
    }

    @GetMapping("/processDefinition")
    @ApiOperation(value = "流程定义列表", notes = "流程定义列表")
    @SuppressWarnings("unchecked")
    public ResponseEntity list(ProcessQuery query) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc();
        if (StringUtils.isNotEmpty(query.getCategory())) {
            processDefinitionQuery.processDefinitionCategory(query.getCategory());
        }
        long total = processDefinitionQuery.count();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(total));
        List<ProcessDefinition> models = processDefinitionQuery.listPage(getFirstResult(query, total), query.getLimit());
        List<Map<String, Object>> pd = processService.getMaps(models);
        return new ResponseEntity(pd, headers, HttpStatus.OK);
    }

    /**
     * start a process with the given key
     *
     * @param processKey the identifier of process
     * @param applyCode  the business key
     * @return nothing
     */
    @PutMapping("/start/{processKey}/{applyCode}")
    @ApiOperation(value = "start a process with the given key.", notes = "applyCode is equals the business key")
    public ResponseEntity start(@PathVariable String processKey, @PathVariable String applyCode) {
        runtimeService.startProcessInstanceByKey(processKey, applyCode);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/searchByKey/{processDefinitionKey}")
    @ApiOperation(value = "根据流程key查询流程实例", notes = "查询流程实例")
    public ResponseEntity searchProcessInstance(@PathVariable String processDefinitionKey) {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        List<ProcessInstance> runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        return ResponseEntity.ok(runningList);
    }


    @GetMapping("/searchById/{processId}")
    @ApiOperation(value = "根据流程ID查询流程实例", notes = "查询流程实例")
    public ResponseEntity searchByID(@PathVariable String processId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        return ResponseEntity.ok(pi);
    }


    @DeleteMapping("/deleteProcessInstance/{processId}")
    @ApiOperation(value = "根据流程实例ID删除流程实例", notes = "根据流程实例ID删除流程实例")
    public ResponseEntity deleteProcessInstanceByID(@PathVariable String processId) {
        runtimeService.deleteProcessInstance(processId, "删除" + processId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/deleteProcessInstanceByKey/{processDefinitionKey}")
    @ApiOperation(value = "根据流程实例key删除流程实例", notes = "根据流程实例key删除流程实例")
    public ResponseEntity deleteProcessInstanceByKey(@PathVariable String processDefinitionKey) {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        List<ProcessInstance> runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        if (CollectionUtil.isNotEmpty(runningList)) {
            runningList.forEach(s -> runtimeService.deleteProcessInstance(s.getId(), "删除"));
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/processImg/{processId}")
    @ApiOperation(value = "根据流程实例id查看流程进度图", notes = "流程进度图")
    public ResponseEntity processImg(@PathVariable("processId") String processId, HttpServletResponse response){
        processService.readProcessResource(processId,response);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/definitionImg/{pdid}/{resourceName}")
    @ApiOperation(value = "根据流程定义id和资源名查看流程资源图", notes = "流程资源图")
    public ResponseEntity  definitionImg(@PathVariable("pdid") String processDefinitionId, @PathVariable("resourceName") String resourceName, HttpServletResponse response) {
        processService.readDefinitionResource(processDefinitionId,resourceName,response);
        return ResponseEntity.ok().build();
    }


    @PostMapping( "/instance/suspendOrActiveApply/{processId}/{suspendState}")
    @ApiOperation(value = "根据流程实例id流程封存和解封", notes = "流程实例封存和解封")
    @ApiParam(required = true, name ="suspendState", value ="1:封存，2：解封")
    public ResponseEntity suspendOrActiveApplyInstance(@PathVariable("processId") String processId, @PathVariable("suspendState") String suspendState) {
        processService.suspendOrActiveApplyInstance(processId, suspendState);
        return ResponseEntity.ok().build();
    }


    @PostMapping( "/definition/suspendOrActiveApply/{definitionId}/{suspendState}")
    @ApiOperation(value = "根据流程定义id流程封存和解封", notes = "流程定义封存和解封")
    @ApiParam(required = true, name ="suspendState", value ="1:封存，2：解封")
    public ResponseEntity suspendOrActiveApplyDefinition(@PathVariable("definitionId") String definitionId, @PathVariable("suspendState") String suspendState) {
        processService.suspendOrActiveApplyDefinition(definitionId, suspendState);
        return ResponseEntity.ok().build();
    }


}