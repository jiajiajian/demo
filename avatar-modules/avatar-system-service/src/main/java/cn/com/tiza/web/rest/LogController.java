package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.Log;
import cn.com.tiza.service.LogService;
import cn.com.tiza.service.dto.LogQuery;
import cn.com.tiza.service.mapper.LogMapper;
import cn.com.tiza.web.rest.dto.LogCommand;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.LogVM;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 0837
 */
@RestController
@RequestMapping("logs")
public class LogController {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private LogService logService;

    @GetMapping
    public ResponseEntity<List<LogVM>> getLogs(LogQuery logQuery) {
        if (logQuery.getOrganizationId() == null) {
            logQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        if (logQuery.getFinanceId() == null) {
            logQuery.setFinanceId(BaseContextHandler.getFinanceId());
        }
        PageQuery<Log> pageQuery = logService.findAll(logQuery);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity<>(logMapper.logListToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid LogCommand log) {
        Log newLog = logService.create(log);
        return ResponseEntity.ok(newLog);
    }
}
