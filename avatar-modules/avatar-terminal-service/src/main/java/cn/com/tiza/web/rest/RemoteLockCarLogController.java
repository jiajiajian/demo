package cn.com.tiza.web.rest;

import cn.com.tiza.domain.Lock;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.service.LockService;
import cn.com.tiza.service.dto.RemoteLockCarLogQuery;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller
 * gen by beetlsql 2020-03-21
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/log")
public class RemoteLockCarLogController {

    @Autowired
    private LockService lockService;

    @GetMapping
    public ResponseEntity<List<Lock>> list(RemoteLockCarLogQuery query) {
        PageQuery<Lock> pageQuery = lockService.findAllLogs(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }


    @GetMapping("/export")
    public ResponseEntity export(HttpServletRequest req, HttpServletResponse res, RemoteLockCarLogQuery query) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE - 1);
        PageQuery<Lock> page = lockService.findAllLogs(query);
        List<Lock> list = page.getList();
        String fileName = "远程锁车日志";
        String[] headers = {"机器序列号", "终端编号", "SIM卡号", "执行指令", "执行状态", "执行时间", "响应时间",
                "所属机构", "车辆类型", "车辆型号", "操作人", "操作IP"};
        String[] columns = {"vin", "terminalCode", "simCode", "itemName", "statusName", "executeTime",
                "responseTime", "orgName", "typeName", "modelName", "operateUsername", "ipAddress"};
        try {
            ExcelWriter.exportExcel(req, res, fileName, headers, columns, list);
        } catch (IOException e) {
            log.error("sim card export exception: the msg is {}", e.getMessage());
            throw new BadRequestAlertException("sim card export exception", null, "export.failed");
        }
        return ResponseEntity.ok().build();
    }

}
