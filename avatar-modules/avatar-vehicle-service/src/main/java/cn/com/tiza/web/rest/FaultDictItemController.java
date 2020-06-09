package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.FaultDictItem;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.FaultDictItemService;
import cn.com.tiza.service.dto.*;
import cn.com.tiza.service.mapper.FaultDictItemMapper;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.FaultDictItemVM;
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
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/faultDictItem")
public class FaultDictItemController extends ExcelController {

    @Autowired
    private FaultDictItemService faultDictItemService;

    @Autowired
    private FaultDictItemMapper faultDictItemMapper;

    @Autowired
    private FileApiClient fileApiClient;

    @GetMapping
    public ResponseEntity<List<FaultDictItemVM>> list(FaultDictItemQuery query) {
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            query.setOrganizationId(BaseContextHandler.getRootOrgId());
        }
        PageQuery pageQuery = faultDictItemService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<FaultDictItem> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(faultDictItemService.get(id));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid FaultDictItemDto dto) {
        FaultDictItem newObj = faultDictItemService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid FaultDictItemDto dto) {
        Optional<FaultDictItem> updatedObj = faultDictItemService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faultDictItemService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        faultDictItemService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 导入
     *
     * @param command
     */
    @PostMapping("/import")
    public void importItem(FaultDictItemImportCommand command) {
        ExcelReader reader = ExcelReader
                .createInstance(2, FaultDictItemImportDto::new, null, FaultDictItemImportDto.ruleValidator());
        try {
            List<FaultDictItemImportDto> list = reader.create(command.getFile().getInputStream()).resolve();
            faultDictItemService.checkImport(reader, list, command.getRootOrgId());
            if (reader.hasError()) {
                String fileId = fileApiClient.save(FileDto.buildTempFile(command.getFile().getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }
            faultDictItemService.importItem(list, command.getRootOrgId());
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private  String[] titles = {"机构", "TLA", "故障码", "故障参数", "故障模式"};

    /**
     * 导出
     */
    @GetMapping("/export")
    public void export(FaultDictItemQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setLimit(Integer.MAX_VALUE);
        query.setPage(1);
        List<FaultDictItemVM> body = this.list(query).getBody();
        download("故障字典", titles, body, FaultDictItemVM::toRow, request, response);
    }
}
