package cn.com.tiza.web.rest;

import cn.com.tiza.annotation.CurrentUser;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.context.access.PermissionCheck;
import cn.com.tiza.domain.Role;
import cn.com.tiza.dto.RoleType;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.RoleService;
import cn.com.tiza.service.SysFunctionService;
import cn.com.tiza.service.dto.AuthorizeCommand;
import cn.com.tiza.service.dto.RoleCommand;
import cn.com.tiza.service.dto.RoleQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.RoleVM;
import io.swagger.annotations.ApiOperation;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.com.tiza.constant.Permissions.Role.*;

/**
 * 角色API
 *
 * @author tiza
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private SysFunctionService sysFunctionService;

    /**
     * 获取单个角色类型
     **/
    @GetMapping("/types")
    public ResponseEntity<List<SelectOption>> types() {
        return ResponseEntity.ok(Arrays.stream(RoleType.values())
                .map(t -> new SelectOption(t.name(), t.getName()))
                .collect(Collectors.toList()));
    }

    @GetMapping
    public ResponseEntity<List<RoleVM>> list(RoleQuery roleQuery,
                                             @CurrentUser UserInfo loginUser) {
        if (loginUser.isOrganization()) {
            //根级组织ID
            roleQuery.setOrganizationId(loginUser.getRootOrgId());
        } else if (loginUser.isFinance()) {
            roleQuery.setFinanceId(loginUser.getFinanceId());
        }
        PageQuery<RoleVM> pageQuery = roleService.findAll(roleQuery);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @PermissionCheck(value = Create.VALUE, description = Create.DESCRIPTION)
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid RoleCommand command,
                                 @CurrentUser UserInfo loginUser) {
        Role role = roleService.create(command, loginUser);
        return ResponseEntity.ok(role);
    }

    /**
     * 获取单个角色详细信息
     **/
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Role> optional = roleService.get(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    /**
     * 更新角色信息
     **/
    @PermissionCheck(value = Update.VALUE, description = Update.DESCRIPTION)
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid RoleCommand command,
                                 @CurrentUser UserInfo loginUser) {
        Optional<Role> optional = roleService.update(id, command, loginUser);
        return ResponseUtil.wrapOrNotFound(optional);
    }

    @PermissionCheck(value = Delete.VALUE, description = Delete.DESCRIPTION)
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("ids") List<Long> ids,
                                       @CurrentUser UserInfo loginUser) {
        roleService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新角色权限
     **/
    @PermissionCheck(value = Auth.VALUE, description = Auth.DESCRIPTION)
    @PutMapping("permission/{id}")
    public ResponseEntity updateAuthorizes(@PathVariable Long id, @RequestBody List<AuthorizeCommand> commands) {
        roleService.updateAuthorizes(id, commands);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("获取菜单树")
    @GetMapping("/tree/{id}")
    public ResponseEntity tree(@PathVariable Long id) {
        return ResponseEntity.ok(sysFunctionService.tree(id));
    }

    /**
     * 下拉框
     *
     * @param organizationId organizationId
     * @param financeId      financeId
     * @return list of roles
     */
    @GetMapping("/options")
    public ResponseEntity<List<SelectOption>> roleOptions(@RequestParam(required = false) Long organizationId,
                                                          @RequestParam(required = false) Long financeId) {
        return ResponseEntity.ok(roleService.selectOptions(organizationId, financeId));
    }

    /**
     * 根据当前用户查询角色下拉选
     *
     * @return
     */
    @GetMapping("/optionsByPresentUser")
    public ResponseEntity<List<SelectOption>> optionsByPresentUser() {
        return ResponseEntity.ok(roleService.optionsByPresentUser());
    }
}
