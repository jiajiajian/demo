package cn.com.tiza.web.rest;

import cn.com.tiza.annotation.CurrentUser;
import cn.com.tiza.constant.Permissions;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.context.access.PermissionCheck;
import cn.com.tiza.domain.Customer;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.CustomerService;
import cn.com.tiza.service.dto.CustomerDto;
import cn.com.tiza.service.dto.CustomerQuery;
import cn.com.tiza.service.mapper.CustomerMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.CustomerVO;
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
import java.util.Optional;

/**
 * Controller
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("customers")
public class CustomerController extends ExcelController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping
    public ResponseEntity<List<CustomerVO>> list(CustomerQuery query,
                                                 @CurrentUser UserInfo loginUser) {
        if (loginUser.isOrganization()) {
            query.setOrganizationId(loginUser.getRootOrgId());
        }
        PageQuery<CustomerVO> pageQuery = customerService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerVO> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(customerService.get(id)
                .map(obj -> customerMapper.toVM(obj)));
    }

    @PermissionCheck(value = Permissions.Customer.Create.VALUE, description = Permissions.Customer.Create.DESCRIPTION)
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid CustomerDto dto,
                                 @CurrentUser UserInfo loginUser) {
        Customer newObj = customerService.create(dto, loginUser);
        return ResponseEntity.ok(newObj);
    }

    @PermissionCheck(value = Permissions.Customer.Update.VALUE, description = Permissions.Customer.Update.DESCRIPTION)
    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid CustomerDto dto,
                                 @CurrentUser UserInfo loginUser) {
        Optional<Customer> updatedObj = customerService.update(id, dto, loginUser);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @PermissionCheck(value = Permissions.Customer.Delete.VALUE, description = Permissions.Customer.Delete.DESCRIPTION)
    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids,
                                       @CurrentUser UserInfo loginUser) {
        customerService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 机构客户列表
     *
     * @param orgId
     * @return
     */
    @GetMapping("options/{orgId}")
    public ResponseEntity<List<SelectOption>> optionsByOrgId(@PathVariable Long orgId) {
        return ResponseEntity.ok(customerService.optionsByOrgId(orgId));
    }

    String[] headers = new String[]{"客户名称", "客户电话", "所属机构", "机手", "机手电话", "报警联系人", "24H报警电话", "创建人", "创建时间"};

    /**
     * 导出
     *
     * @param query    params
     * @param request  req
     * @param response resp
     */
//    @PermissionCheck(value = Permissions.Finance.Ex.VALUE, description = Permissions.Finance.Delete.DESCRIPTION)
    @GetMapping("export")
    public void export(CustomerQuery query, HttpServletRequest request, HttpServletResponse response) {
        List<CustomerVO> data = customerService.exportQuery(query);
        download("融资机构", headers, data, CustomerVO::toRow, request, response);
    }

    /**
     * 当前用户 客户下拉选
     *
     * @return
     */
    @GetMapping("/options")
    public ResponseEntity<List<SelectOption>> options() {
        return ResponseEntity.ok(customerService.optionsByOrgId(BaseContextHandler.getRootOrgId()));
    }

    @GetMapping("/optionsBySelectOrg/{orgId}")
    public ResponseEntity<List<SelectOption>> optionsBySelectOrg(@PathVariable Long orgId) {
        return ResponseEntity.ok(customerService.optionsBySelectOrg(orgId));
    }

}
