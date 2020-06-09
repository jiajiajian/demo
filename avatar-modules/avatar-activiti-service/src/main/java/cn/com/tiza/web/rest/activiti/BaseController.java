package cn.com.tiza.web.rest.activiti;

import org.activiti.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author villas
 */
@Controller
public class BaseController {

    @Autowired
    public TaskService taskService;

    @Autowired
    public RuntimeService runtimeService;

    @Autowired
    public HistoryService historyService;

    @Autowired
    public RepositoryService repositoryService;

}
