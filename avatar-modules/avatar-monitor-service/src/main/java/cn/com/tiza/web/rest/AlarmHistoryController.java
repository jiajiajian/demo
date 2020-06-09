package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.dao.AlarmInfoDao;
import cn.com.tiza.dao.TlaDao;
import cn.com.tiza.domain.Tla;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.AlarmHistoryService;
import cn.com.tiza.service.dto.AlarmHistoryQuery;
import cn.com.tiza.service.dto.FenceAlarmQuery;
import cn.com.tiza.service.jobs.VehicleClient;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.AlarmHistoryVM;
import cn.com.tiza.web.rest.vm.FenceAlarmVM;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller
 * gen by beetlsql 2020-03-23
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/alarmHistory")
public class AlarmHistoryController extends ExcelController {

    @Autowired
    private AlarmHistoryService alarmHistoryService;

    @Autowired
    private VehicleClient vehicleClient;

    @Autowired
    private AlarmHistoryDao historyDao;

    @Autowired
    private AlarmInfoDao alarmInfoDao;

    @Autowired
    private TlaDao tlaDao;


    @GetMapping("/pageQueryAlarm")
    public ResponseEntity<List<AlarmHistoryVM>> pageQueryAlarm(AlarmHistoryQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery<AlarmHistoryVM> pageQuery = alarmHistoryService.pageQueryAlarm(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/pageQuerySingleVehicleAlarm/{vin}")
    public ResponseEntity<List<AlarmHistoryVM>> pageQuerySingleVehicleAlarm(AlarmHistoryQuery query, @PathVariable String vin) {
        query.setVin(vin);
        PageQuery<AlarmHistoryVM> pageQuery = alarmHistoryService.pageQuerySingleVehicleAlarm(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    private String[] alarmTitles = {"机器序列号", "终端编号", "SIM卡号", "报警种类", "最新时间", "最新位置"};

    @GetMapping("/exportAlarm")
    public void exportAlarm(AlarmHistoryQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        List<AlarmHistoryVM> list = alarmHistoryService.pageQueryAlarm(query).getList();
        download("报警", alarmTitles, list, AlarmHistoryVM::toAlarmRow, request, response);
    }

    private String[] singleVehicleAlarmTitles = {"发生时间", "解除时间", "报警项", "发生地点"};

    @GetMapping("/exportSingleVehicleAlarm/{vin}")
    public void exportSingleVehicleAlarm(@PathVariable String vin, AlarmHistoryQuery query,
                                         HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        query.setVin(vin);
        List<AlarmHistoryVM> list = alarmHistoryService.pageQuerySingleVehicleAlarm(query).getList();
        download(vin + "报警", singleVehicleAlarmTitles, list, AlarmHistoryVM::toSingleVehicleAlarmRow, request, response);
    }

    @GetMapping("/pageQueryFault")
    public ResponseEntity<List<AlarmHistoryVM>> pageQueryFault(AlarmHistoryQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery<AlarmHistoryVM> pageQuery = alarmHistoryService.pageQueryFault(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/pageQuerySingleVehicleFault/{vin}")
    public ResponseEntity<List<AlarmHistoryVM>> pageQuerySingleVehicleFault(AlarmHistoryQuery query, @PathVariable String vin) {
        query.setVin(vin);
        Long rootOrg = vehicleClient.rootOrgByVin(vin);
        if (Objects.isNull(rootOrg)) {
            PageQuery<AlarmHistoryVM> vmPageQuery = new PageQuery<>();
            vmPageQuery.setList(Lists.newArrayList());
            vmPageQuery.setTotalRow(0);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(vmPageQuery);
            return new ResponseEntity(vmPageQuery.getList(), headers, HttpStatus.OK);
        }
        query.setRootOrg(rootOrg);
        PageQuery<AlarmHistoryVM> pageQuery = alarmHistoryService.pageQuerySingleVehicleFault(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    /**
     * 车辆历史故障
     *
     * @param query
     * @param vin
     * @return
     */
    @GetMapping("/pageQuerySingleVehicleHistoryFault/{vin}")
    public ResponseEntity<List<Map>> pageQuerySingleVehicleHistoryFault(AlarmHistoryQuery query, @PathVariable String vin) {
        query.setVin(vin);
        Long rootOrg = vehicleClient.rootOrgByVin(vin);
        if (Objects.isNull(rootOrg)) {
            PageQuery<Object> vmPageQuery = new PageQuery<>();
            vmPageQuery.setList(Lists.newArrayList());
            vmPageQuery.setTotalRow(0);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(vmPageQuery);
            return new ResponseEntity(vmPageQuery.getList(), headers, HttpStatus.OK);
        }
        query.setRootOrg(rootOrg);
        PageQuery<Map> pageQuery = alarmHistoryService.pageQuerySingleVehicleHistoryFault(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }


    private String[] faultTitles = {"机器序列号", "终端编号", "SIM卡号", "故障种类", "MTU故障次数", "最新时间", "最新位置"};

    @GetMapping("/exportFault")
    public void exportFault(AlarmHistoryQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        List<AlarmHistoryVM> list = this.pageQueryFault(query).getBody();
        download("故障", faultTitles, list, AlarmHistoryVM::toFaultRow, request, response);
    }

    private String[] singleVehicleFaultTitles = {"发生时间", "解除时间", "故障码", "TLA", "故障参数", "故障模式", "故障次数", "发生地点"};
    private String[] singleVehicleFaultTitles1 = {"发生时间", "解除时间", "故障码", "故障参数", "故障次数", "发生地点"};

    @GetMapping("/exportSingleVehicleFault/{vin}")
    public void exportSingleVehicleFault(@PathVariable String vin, AlarmHistoryQuery query,
                                         HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<AlarmHistoryVM> list = this.pageQuerySingleVehicleFault(query, vin).getBody();
        int size = tlaDao.createLambdaQuery()
                .andEq(Tla::getOrganizationId, query.getRootOrg())
                .select().size();
        if (size > 0) {
            download(vin + "故障", singleVehicleFaultTitles, list, AlarmHistoryVM::toSingleVehicleFaultRow, request, response);
        } else {
            download(vin + "故障", singleVehicleFaultTitles1, list, AlarmHistoryVM::toSingleVehicleFaultRow1, request, response);
        }
    }


    @GetMapping("/pageQueryFence")
    public ResponseEntity<List<FenceAlarmVM>> pageQueryFence(FenceAlarmQuery query) {
        if (Objects.isNull(query.getOrganizationId()) && BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        if (Objects.isNull(query.getFinanceId()) && BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            query.setOrganizationId(BaseContextHandler.getFinanceId());
        }
        PageQuery<FenceAlarmVM> pageQuery = alarmHistoryService.pageQueryFence(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    private String[] fenceTitles = {"机器序列号", "所属机构", "围栏类型", "报警类型", "发生地点", "发生时间", "解除时间"};

    @GetMapping("/exportFence")
    public void exportFence(FenceAlarmQuery query, HttpServletRequest request, HttpServletResponse response) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<FenceAlarmVM> list = this.pageQueryFence(query).getBody();
        download("围栏报警", fenceTitles, list, FenceAlarmVM::toFenceRow, request, response);
    }

    @GetMapping("/pageQuerySingleVehicleFence/{vin}")
    public ResponseEntity<List<FenceAlarmVM>> pageQuerySingleVehicleFence(FenceAlarmQuery query, @PathVariable String vin) {
        query.setVin(vin);
        PageQuery<FenceAlarmVM> pageQuery = alarmHistoryService.pageQuerySingleVehicleFence(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    private String[] singleVehicleFenceTitles = {"发生时间", "解除时间", "围栏类型", "报警类型", "发生地点"};

    @GetMapping("/exportSingleVehicleFence/{vin}")
    public void exportSingleVehicleFence(@PathVariable String vin, FenceAlarmQuery query,
                                         HttpServletRequest request, HttpServletResponse response) {
        query.setVin(vin);
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<FenceAlarmVM> list = alarmHistoryService.pageQuerySingleVehicleFence(query).getList();
        download(vin + "围栏报警", singleVehicleFenceTitles, list, FenceAlarmVM::toSingleVehicleFenceRow, request, response);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> count() {
        return ResponseEntity.ok(alarmHistoryService.count());
    }

    @DeleteMapping("/deleteByVin/{vin}")
    public void deleteByVin(@PathVariable String vin) {
        historyDao.deleteByVin(vin);
        alarmInfoDao.deleteByVin(vin);
    }
}


