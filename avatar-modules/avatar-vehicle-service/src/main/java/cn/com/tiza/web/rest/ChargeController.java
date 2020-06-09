package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.Charge;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.ChargeService;
import cn.com.tiza.service.dto.*;
import cn.com.tiza.service.mapper.ChargeMapper;
import cn.com.tiza.util.LocalDateTimeUtils;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.*;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * Controller
 * gen by beetlsql 2020-04-20
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/charge")
public class ChargeController extends ExcelController {

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ChargeMapper chargeMapper;

    @GetMapping
    public ResponseEntity<List<ChargeVM>> list(ChargeQuery query) {
        if (Objects.nonNull(BaseContextHandler.getRootOrgId())) {
            query.setOrganizationId(BaseContextHandler.getRootOrgId());
        }
        PageQuery<ChargeVM> pageQuery = chargeService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ChargeVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(chargeService.get(id)
                .map(obj -> chargeMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create( @Valid @RequestBody ChargeDto dto) {
        Charge newObj = chargeService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    /**
     * 获取费用配置详情
     *
     * @param chargeId
     * @return
     */
    @GetMapping("/getChargeConfig/{chargeId}")
    public ResponseEntity<ChargeConfigVM> getChargeConfig(@PathVariable Long chargeId) {
        return ResponseEntity.ok(chargeService.getChargeConfig(chargeId));
    }

    /**
     * 新增配置
     * @param chargeId
     * @param dto
     */
    @PutMapping("/addConfigDetail/{chargeId}")
    public void addConfigDetail(@PathVariable Long chargeId, @RequestBody ChargeDetailDto dto) {
        chargeService.addConfigDetail(chargeId, dto);
    }

    /**
     * 修改配置
     * @param chargeDetailId
     * @param dto
     */
    @PutMapping("/updateConfigDetail/{chargeDetailId}")
    public void updateConfigDetail(@PathVariable Long chargeDetailId, @RequestBody ChargeDetailDto dto) {
        chargeService.updateConfigDetail(chargeDetailId, dto);
    }

    /**
     * 删除配置
     * @param ids
     */
    @DeleteMapping("/deleteConfigDetail")
    public void deleteConfigDetail(@RequestParam Long[] ids){
        chargeService.deleteConfigDetail((ids));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chargeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        chargeService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pageQueryPrePaid")
    public ResponseEntity<List<PrePaidVM>> pageQueryPrePaid(PrepaidQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setBeginTime(LocalDateTimeUtils.getMonStartDayTime());
        query.setEndTime(LocalDateTimeUtils.getMonStartEndTime());
        query.setBeginDate(Integer.parseInt(LocalDateTimeUtils.formatDay(query.getBeginTime(), "yyyyMMdd")));
        query.setEndDate(Integer.parseInt(LocalDateTimeUtils.formatDay(query.getEndTime(), "yyyyMMdd")));
        PageQuery<PrePaidVM> prePaidVMPageQuery = chargeService.pageQueryPrePaid(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(prePaidVMPageQuery);
        return new ResponseEntity(prePaidVMPageQuery.getList(), headers, HttpStatus.OK);
    }

    private String[] titles = {"机器序列号", "SIM卡号", "服务开始", "服务期限", "服务结束", "服务状态", "代理商", "总部",
            "是否收费", "信息服务费", "是否本月新入网", "本月是否续费", "续费时长", "续费金额"};

    @GetMapping("/exportPrePaid")
    public void export(PrepaidQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<PrePaidVM> list = this.pageQueryPrePaid(query).getBody();
        download("结算清单(预付费)", titles, list, PrePaidVM::toRow, request, response);
    }

    @GetMapping("/pageQueryAfterPaid")
    public ResponseEntity<List<AfterPaidVM>> afterPaidQuery(AfterPaidQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery<AfterPaidVM> afterPaidQuery = chargeService.afterPaidQuery(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(afterPaidQuery);
        return new ResponseEntity(afterPaidQuery.getList(), headers, HttpStatus.OK);
    }

    private String[] afterPaidTitles = {"机器序列号", "SIM卡号", "总部", "入网时间", "服务截止时间", "已收费月份", "本次收费月份", "未收费月份",
            "单价(元)", "本次收费金额(元)"};

    @GetMapping("/exportAfterPaid")
    public void exportAfterPaid(AfterPaidQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<AfterPaidVM> list = this.afterPaidQuery(query).getBody();
        download("结算清单(后付费)", afterPaidTitles, list, AfterPaidVM::toRow, request, response);
    }

    @GetMapping("/pageQueryServiceWarn")
    public ResponseEntity<List<ServiceExpireWarnVM>> pageQueryServiceWarn(PrepaidQuery query) {
        if (Objects.nonNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setBeginTime(LocalDateTimeUtils.getMonStartDayTime());
        query.setEndTime(LocalDateTimeUtils.getMonStartEndTime());
        query.setBeginDate(Integer.parseInt(LocalDateTimeUtils.formatDay(query.getBeginTime(), "yyyyMMdd")));
        query.setEndDate(Integer.parseInt(LocalDateTimeUtils.formatDay(query.getEndTime(), "yyyyMMdd")));
        PageQuery<ServiceExpireWarnVM> prePaidVMPageQuery = chargeService.pageQueryServiceWarn(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(prePaidVMPageQuery);
        return new ResponseEntity(prePaidVMPageQuery.getList(), headers, HttpStatus.OK);
    }

    private String[] serviceWarnTitles = {"机器序列号", "终端编号", "SIM卡号", "所属机构", "服务到期时间"};

    @GetMapping("/exportServiceWarn")
    public void exportServiceWarn(PrepaidQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<ServiceExpireWarnVM> list = this.pageQueryServiceWarn(query).getBody();
        download("服务到期提醒", serviceWarnTitles, list, ServiceExpireWarnVM::toRow, request, response);
    }

    @GetMapping("/dashboardWarnNum")
    public ResponseEntity<Integer> dashboardWarnNum() {
        Long monStartDayTime = LocalDateTimeUtils.getMonStartDayTime();
        Long monStartEndTime = LocalDateTimeUtils.getMonStartEndTime();
        int beginDate = Integer.parseInt(LocalDateTimeUtils.formatDay(monStartDayTime, "yyyyMMdd"));
        int endDate = Integer.parseInt(LocalDateTimeUtils.formatDay(monStartEndTime, "yyyyMMdd"));
        return ResponseEntity.ok(chargeService.dashboardWarn(beginDate, endDate));
    }

    @PutMapping("/closeWarn")
    public ResponseEntity<Void> closeWarn() {
        chargeService.closeWarn();
        return ResponseEntity.ok().build();
    }
}
