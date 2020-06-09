package cn.com.tiza.web.rest;

import cn.com.tiza.domain.DicItem;
import cn.com.tiza.domain.Terminal;
import cn.com.tiza.excel.DataType;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.excel.read.CellRule;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.service.TerminalService;
import cn.com.tiza.service.dto.FileDto;
import cn.com.tiza.service.dto.TerminalDto;
import cn.com.tiza.service.dto.TerminalQuery;
import cn.com.tiza.service.mapper.TerminalMapper;
import cn.com.tiza.util.ToolUtil;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.vm.TerminalVM;
import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.collection.ListUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    private FileApiClient fileApiClient;

    @GetMapping
    @SuppressWarnings("all")
    public ResponseEntity<List<TerminalVM>> list(TerminalQuery query) {
        PageQuery pageQuery = terminalService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(terminalMapper.entitiesToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TerminalDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(terminalService.getOfUpdate(id));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid TerminalDto dto) {
        terminalService.checkUnique(dto.getCode());
        Terminal newObj = terminalService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid TerminalDto dto) {
        terminalService.checkUnique(id, dto.getCode());
        Optional<Terminal> updatedObj = terminalService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        terminalService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        terminalService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/options/{code}")
    public ResponseEntity<List<Map<String, Object>>> getOptionsByType(@PathVariable String code) {
        List<DicItem> items = terminalService.getOptionsByType(code);
        if (ListUtil.isEmpty(items)) {
            return ResponseEntity.ok(Lists.newArrayList());
        }
        List<Map<String, Object>> res = dictItemMapping(items);
        return ResponseEntity.ok(res);
    }

    private List<Map<String, Object>> dictItemMapping(List<DicItem> items) {
        return items.stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>(ToolUtil.computeMap(4));
                    map.put("id", item.getId());
                    map.put("name", item.getItemName());
                    map.put("value", item.getItemValue());
                    map.put("code", item.getItemCode());
                    return map;
                }).collect(Collectors.toList());
    }

    @GetMapping("/options")
    public ResponseEntity<List<Map<String, Object>>> getOptionsByTypes(@RequestParam List<String> codes) {
        List<DicItem> items = terminalService.getOptionsByTypes(codes);
        if (ListUtil.isEmpty(items)) {
            return ResponseEntity.ok(Lists.newArrayList());
        }
        List<Map<String, Object>> res = dictItemMapping(items);
        return ResponseEntity.ok(res);
    }

    /**
     * 文件导出
     */
    @GetMapping("/exportTerminals")
    public ResponseEntity<Void> exportTerminals(HttpServletRequest request, HttpServletResponse response,
                                                TerminalQuery query) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE - 1);
        PageQuery<Terminal> all = terminalService.findAll(query);
        String[] headers = {"终端编号", "SIM卡号", "通信协议", "软件版本", "硬件版本", "终端型号", "采集功能集",
                "锁车功能集", "生产日期", "操作时间", "操作人"};
        String[] columns = {"code", "simCode", "protocolName", "softName", "firmWireVersion", "terminalModel",
                "collectName", "lockName", "produceDateFormat", "createTimeFormat", "createUserAccount"};
        List<Terminal> content = all.getList();
        try {
            ExcelWriter.exportExcel(request, response, "终端信息", headers, columns, content);
        } catch (IOException e) {
            log.error("terminal export exception, the msg is {}", e.getMessage());
            throw new BadRequestAlertException("terminal export exception", null, "export.failed");
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("import")
    public ResponseEntity<Integer> importTerminals(@RequestParam MultipartFile file) throws IOException {
        //excel文件校验头
        String[] titles = {"* 终端编号", "* SIM卡号", "* 软件版本", "* 通信协议", "* 硬件版本", "* 终端型号", "* 机构名称"};
        //获取 excel reader
        try (ExcelReader<TerminalDto> reader = ExcelReader.createInstance(TerminalDto::new, titles,
                rules -> {
                    //校验规则
                    rules.add(new CellRule(1, DataType.STRING, true));
                    rules.add(new CellRule(2, DataType.LONG, true));
                    rules.add(new CellRule(3, DataType.STRING, true));
                    rules.add(new CellRule(4, DataType.STRING, true));
                    rules.add(new CellRule(5, DataType.STRING, true));
                    rules.add(new CellRule(6, DataType.STRING, true));
                    rules.add(new CellRule(7, DataType.STRING, true));
                })) {
            //读取数据，并校验（如是否重复，数据库唯一等）
            List<TerminalDto> dtoList = terminalService.readTerminal(reader, file.getInputStream());
            //如有错误回写错误文件
            if (reader.hasError()) {
                String fileId = fileApiClient.save(FileDto.buildTempFile(file.getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }

            //保存数据
            if (!dtoList.isEmpty()) {
                terminalService.save(dtoList);
            }
            return ResponseEntity.ok(dtoList.size());
        }

    }


}
