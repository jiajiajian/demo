package cn.com.tiza.web.rest;

import cn.com.tiza.domain.Lock;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.service.LockService;
import cn.com.tiza.service.dto.CmdQuery;
import cn.com.tiza.service.dto.LockQuery;
import cn.com.tiza.web.rest.dto.VehicleVM;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.vm.LockVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/lock")
@SuppressWarnings("unchecked")
public class LockController {

    @Autowired
    private LockService lockService;

    @GetMapping
    public ResponseEntity<List<Lock>> list(LockQuery query) {
        PageQuery pageQuery = lockService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/getVehicleList")
    public ResponseEntity<List<VehicleVM>> getVehicleList(LockQuery query) {
        PageQuery pageQuery = lockService.getVehicleList(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestParam String[] vins) {
        if (vins.length != 0) {
            lockService.create(vins);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        lockService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 指令下发
     */
    @PostMapping("/cmdSend")
    public ResponseEntity<Void> cmdSend(@RequestBody List<CmdQuery> params) {
        lockService.cmdSend(params);
        return ResponseEntity.ok().build();
    }

    /**
     * 文件导出
     */
    @GetMapping("/export")
    public ResponseEntity<Void> exportLock(HttpServletRequest req, HttpServletResponse res, LockQuery query) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE - 1);
        PageQuery pageQuery = lockService.findAll(query);
        List<Lock> lockList = pageQuery.getList();
        String fileName = "远程锁车";
        String[] headers = {"机器序列号", "终端编号", "SIM卡号", "执行指令", "执行状态", "执行时间", "响应时间", "ACC状态",
                "定位状态", "终端状态", "所属机构", "车辆类型", "车辆型号", "销售状态", "客户名称", "客户电话"};
        String[] columns = {"vin", "terminalCode", "simCode", "itemName", "runStateName", "executeTime",
                "responseTime", "accName", "gpsName", "terminalStatus", "orgName", "typeName", "modelName",
                "saleStatusName", "customerName", "phoneNumber"};
        try {
            ExcelWriter.exportExcel(req, res, fileName, headers, columns, lockList);
        } catch (IOException e) {
            log.error("Lock car export exception: the msg is {}", e.getMessage());
            throw new BadRequestAlertException("Lock car export exception", null, "export.failed");
        }
        return ResponseEntity.ok().build();
    }

}
