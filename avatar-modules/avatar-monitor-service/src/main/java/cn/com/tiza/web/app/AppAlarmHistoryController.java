package cn.com.tiza.web.app;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.service.AlarmHistoryService;
import cn.com.tiza.service.AlarmInfoService;
import cn.com.tiza.service.dto.AlarmHistoryQuery;
import cn.com.tiza.service.jobs.VehicleClient;
import cn.com.tiza.web.app.vm.AlarmCountVM;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.AlarmHistoryVM;
import cn.com.tiza.web.rest.vm.AlarmInfoVM;
import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.collection.ArrayUtil;
import com.vip.vjtools.vjkit.collection.ListUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.xml.ws.Response;
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
@RequestMapping("/app/alarmHistory")
public class AppAlarmHistoryController {

    @Autowired
    private AlarmHistoryService alarmHistoryService;

    @Autowired
    private VehicleClient vehicleClient;

    /**
     * 车辆当前故障
     * @param query
     * @param vin
     * @return
     */
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
     * 车辆当前报警
     * @param query
     * @param vin
     * @return
     */
    @GetMapping("/pageQuerySingleVehicleAlarm/{vin}")
    public ResponseEntity<List<AlarmHistoryVM>> pageQuerySingleVehicleAlarm(AlarmHistoryQuery query, @PathVariable String vin) {
        query.setVin(vin);
        PageQuery<AlarmHistoryVM> pageQuery = alarmHistoryService.pageQuerySingleVehicleAlarm(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    /**
     * 获取故障详情
     * @param id
     * @return
     */
    @GetMapping("/pageQuerySingleVehicleFaultDetail/{id}")
    public ResponseEntity<AlarmHistoryVM> getVehicleFaultDetail(@PathVariable("id") Long id) {
        AlarmHistoryVM vm = alarmHistoryService.getVehicleFaultDetail(id);
        return ResponseEntity.ok(vm);
    }

    /**
     * 获取报警详情列表
     * @param id
     * @return
     */
    @GetMapping("/pageQuerySingleVehicleAlarmDetail/{id}")
    public ResponseEntity<AlarmHistoryVM> getVehicleAlarmDetail(@PathVariable("id") Long id) {
        AlarmHistoryVM vm = alarmHistoryService.getVehicleAlarmDetail(id);
        return ResponseEntity.ok(vm);
    }

    /**
     * app 报警故障
     * @param query
     * @return
     */
    @GetMapping("/pageQueryAlarmFaultList")
    public ResponseEntity<List<AlarmHistoryVM>> pageQueryAlarmFaultList(AlarmHistoryQuery query) {
        PageQuery<AlarmHistoryVM> pageQuery = alarmHistoryService.pageQueryAlarmFaultList(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    /**
     * 未解除数量
     * @param query
     * @return
     */
    @GetMapping("/getAlarmCountUnDeal")
    public ResponseEntity<Integer> count(AlarmHistoryQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        return ResponseEntity.ok(alarmHistoryService.countUnDealAlarmOrFault(query));
    }
}