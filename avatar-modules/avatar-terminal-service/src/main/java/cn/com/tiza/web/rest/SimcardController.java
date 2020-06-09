package cn.com.tiza.web.rest;

import cn.com.tiza.domain.Simcard;
import cn.com.tiza.excel.DataType;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.excel.read.CellRule;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.service.SimcardService;
import cn.com.tiza.service.dto.FileDto;
import cn.com.tiza.service.dto.SimcardDto;
import cn.com.tiza.service.dto.SimcardQuery;
import cn.com.tiza.service.mapper.SimcardMapper;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.vm.SimcardVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/sim/card")
public class SimcardController {

    @Autowired
    private SimcardService simcardService;

    @Autowired
    private SimcardMapper simcardMapper;

    @Autowired
    private FileApiClient fileApiClient;

    @GetMapping
    @SuppressWarnings("all")
    public ResponseEntity<List<Simcard>> list(SimcardQuery query) {
        PageQuery pageQuery = simcardService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<SimcardVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(simcardService.get(id)
                .map(obj -> simcardMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid SimcardDto dto) {
        simcardService.checkUnique(dto.getCode());
        Simcard newObj = simcardService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid SimcardDto dto) {
        simcardService.checkUnique(id, dto.getCode());
        Optional<Simcard> updatedObj = simcardService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @PutMapping("/abandon/{id}")
    public ResponseEntity abandon(@PathVariable Long id) {
        simcardService.abandon(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        simcardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        simcardService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据终端 修改SIM卡状态 且删除该终端
     *
     * @param terminalId
     * @return
     */
    @PutMapping("/resetCardStatusAndDeleteTerminal/{terminalId}/{cardStatus}")
    public boolean resetCardStatusAndDeleteTerminal(@PathVariable("terminalId") Long terminalId,
                                                    @PathVariable("cardStatus") Integer cardStatus) {
        return simcardService.resetCardStatusAndDeleteTerminal(terminalId, cardStatus);
    }

    @GetMapping("/export")
    public ResponseEntity export(HttpServletRequest req, HttpServletResponse res, SimcardQuery query) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE - 1);
        PageQuery<Simcard> page = simcardService.findAll(query);
        List<Simcard> list = page.getList();
        String fileName = "SIM卡信息";
        String[] headers = {"SIM卡号", "服务状态", "服务开始日期", "服务结束日期", "办卡方式", "办卡客户",
                "销售订单号", "事业部", "运营商", "导入日期"};
        String[] columns = {"code", "serviceState", "serviceStartDateFormat", "serviceEndDateFormat", "carWay",
                "cardOwner", "orderNo", "department", "operator", "createTimeFormat"};
        try {
            ExcelWriter.exportExcel(req, res, fileName, headers, columns, list, s -> {
                s.setServiceStartDateFormat(format(s.getServiceStartDate()));
                s.setServiceEndDateFormat(format(s.getServiceEndDate()));
                s.setCreateTimeFormat(ExcelWriter.timeConvert(s.getCreateTime()));
                return s;
            });
        } catch (IOException e) {
            log.error("sim card export exception: the msg is {}", e.getMessage());
            throw new BadRequestAlertException("sim card export exception", null, "export.failed");
        }
        return ResponseEntity.ok().build();
    }

    private static String format(Integer time) {
        if (Objects.isNull(time)) {
            return "";
        }
        String s = time.toString();
        return s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6);
    }

    /**
     * 文件导入
     */
    @PostMapping("/import")
    public ResponseEntity<Integer> importSimCards(@RequestParam MultipartFile file) {

        String[] titles = {"*卡号", "办卡客户", "销售订单号", "事业部", "运营商", "* 办卡方式"};
        try (ExcelReader<SimcardDto> reader = ExcelReader.createInstance(SimcardDto::new, titles,
                rules -> {
                    //校验规则
                    rules.add(new CellRule(1, DataType.STRING, true));
                    rules.add(new CellRule(6, DataType.STRING, true));
                })) {

            List<SimcardDto> simCardDtoList = simcardService.readSimCards(reader, file.getInputStream());
            //如有错误回写错误文件
            if (reader.hasError()) {
                String fileId = fileApiClient.save(FileDto.buildTempFile(file.getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }

            //保存数据
            if (!simCardDtoList.isEmpty()) {
                simcardService.save(simCardDtoList);
            }
            return ResponseEntity.ok(simCardDtoList.size());
        } catch (IOException e) {
            log.error("SimCard import exception, the msg is : {}" + e.getMessage());
        }
        return ResponseEntity.ok(0);
    }

}
