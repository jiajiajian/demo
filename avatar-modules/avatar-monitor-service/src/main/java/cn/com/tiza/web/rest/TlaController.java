package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.Tla;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.service.TlaService;
import cn.com.tiza.service.dto.DTCEffectQuery;
import cn.com.tiza.service.dto.TlaDto;
import cn.com.tiza.service.dto.TlaQuery;
import cn.com.tiza.service.mapper.TlaMapper;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.DTCEffectVm;
import cn.com.tiza.web.rest.vm.TlaVM;
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
 * gen by beetlsql 2020-05-12
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/tla")
public class TlaController {

    @Autowired
    private TlaService tlaService;

    @Autowired
    private TlaMapper tlaMapper;

    @GetMapping
    public ResponseEntity<List<TlaVM>> list(TlaQuery query) {
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            query.setOrganizationId(BaseContextHandler.getRootOrgId());
        } else if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            return null;
        }
        PageQuery pageQuery = tlaService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TlaVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(tlaService.get(id)
                .map(obj -> tlaMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid TlaDto dto) {
        Tla newObj = tlaService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid TlaDto dto) {
        Optional<Tla> updatedObj = tlaService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tlaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        tlaService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("dtc/affect/list")
    public ResponseEntity<List<DTCEffectVm>> dtcAffectList(DTCEffectQuery query) {
        if (query.getOrganizationId() == null) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery pageQuery = tlaService.dtcAffectList(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/exportTerminals")
    public ResponseEntity<Void> exportDtcAffectList(HttpServletRequest request, HttpServletResponse response,
                                                    DTCEffectQuery query) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE - 1);
        PageQuery<DTCEffectVm> all = tlaService.dtcAffectList(query);
        String[] headers = {"故障代码", "TLA", "故障参数", "故障模式", "影响车辆数"};
        String[] columns = {"spnFmi", "tla", "spnName", "fmiName", "num"};
        List<DTCEffectVm> content = all.getList();
        try {
            ExcelWriter.exportExcel(request, response, "DTC影响统计", headers, columns, content);
        } catch (IOException e) {
            log.error("exportDtcAffectList export exception, the msg is {}", e.getMessage());
            throw new BadRequestAlertException("terminal export exception", null, "export.failed");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tlaOptions/{rootOrgId}")
    public ResponseEntity<List<SelectOption>> tlaOptions(@PathVariable Long rootOrgId) {
        return ResponseEntity.ok(tlaService.tlaOptions(rootOrgId));
    }
}
