package cn.com.tiza.web.rest;

import cn.com.tiza.constant.Permissions;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.access.PermissionCheck;
import cn.com.tiza.domain.Finance;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.FinanceService;
import cn.com.tiza.service.dto.FinanceDto;
import cn.com.tiza.service.dto.FinanceQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.FinanceVM;
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
import java.util.Optional;

/**
 * 融资机构 Controller
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("finances")
public class FinanceController extends ExcelController {

    @Autowired
    private FinanceService financeService;

    /**
     * page query
     *
     * @param query paras
     * @return list
     */
    @GetMapping
    public ResponseEntity<List<FinanceVM>> list(FinanceQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        PageQuery<FinanceVM> pageQuery = financeService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    /**
     * get
     *
     * @param id id
     * @return Finance Object
     */
    @GetMapping("{id}")
    public ResponseEntity<FinanceVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(financeService.getFinance(id));
    }

    @PermissionCheck(value = Permissions.Finance.Create.VALUE, description = Permissions.Finance.Create.DESCRIPTION)
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid FinanceDto dto) {
        financeService.checkNameUnique(null, dto.getName());
        Finance newObj = financeService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PermissionCheck(value = Permissions.Finance.Update.VALUE, description = Permissions.Finance.Update.DESCRIPTION)
    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid FinanceDto dto) {
        Optional<Finance> updatedObj = financeService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @PermissionCheck(value = Permissions.Finance.Delete.VALUE, description = Permissions.Finance.Delete.DESCRIPTION)
    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        financeService.delete(ids);
        return ResponseEntity.ok().build();
    }

    String[] headers = new String[]{"融资机构名称", "关联机构", "机构地址", "联系人", "联系电话", "电子邮箱", "创建人", "创建时间"};

    /**
     * 导出
     *
     * @param query    params
     * @param request  req
     * @param response resp
     */
//    @PermissionCheck(value = Permissions.Finance.Ex.VALUE, description = Permissions.Finance.Delete.DESCRIPTION)
    @GetMapping("export")
    public void export(FinanceQuery query, HttpServletRequest request, HttpServletResponse response) {
        List<FinanceVM> data = financeService.exportQuery(query);
        download("融资机构", headers, data, FinanceVM::toRow, request, response);
    }

    /**
     * 查询当前用户 融资机构下拉选
     *
     * @return
     */
    @GetMapping("/options")
    public ResponseEntity<List<SelectOption>> options() {
        return ResponseEntity.ok(financeService.findOptionsByOrgId(BaseContextHandler.getRootOrgId()));
    }

    /**
     * 根据传递的组织id查询融资机构下拉选
     *
     * @return
     */
    @GetMapping("/optionsBySelectOrg/{orgId}")
    public ResponseEntity<List<SelectOption>> optionsBySelectOrg(@PathVariable Long orgId) {
        return ResponseEntity.ok(financeService.findOptionsByOrgId(orgId));
    }
}
