package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.access.PermissionCheck;
import cn.com.tiza.domain.User;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.UserService;
import cn.com.tiza.service.dto.UserCommand;
import cn.com.tiza.service.dto.UserQuery;
import cn.com.tiza.service.mapper.UserMapper;
import cn.com.tiza.web.app.vm.PasswordChangeDTO;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.UserVM;
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

import static cn.com.tiza.constant.Permissions.User.*;

/**
 * 用户管理
 *
 * @author 0837
 */
@RestController
@RequestMapping("users")
public class UserController extends ExcelController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserVM>> list(UserQuery userQuery) {
        if (Objects.isNull(userQuery.getOrganizationId())) {
            userQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        if (Objects.isNull(userQuery.getFinanceId())) {
            userQuery.setFinanceId(BaseContextHandler.getFinanceId());
        }
        PageQuery<UserVM> pageQuery = userService.findAll(userQuery);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity<>(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @PermissionCheck(value = Create.VALUE, description = Create.DESCRIPTION)
    @PostMapping
    public UserVM create(@RequestBody @Valid UserCommand user) {
        userService.checkLoginNameUnique(null, user.getLoginName());
        User newUser = userService.create(user);
        return userMapper.userToVM(newUser);
    }

    @PermissionCheck(value = Update.VALUE, description = Update.DESCRIPTION)
    @PutMapping("{id}")
    public ResponseEntity<UserVM> update(@PathVariable Long id, @RequestBody @Valid UserCommand user) {
        userService.checkLoginNameUnique(id, user.getLoginName());
        Optional<UserVM> updatedUser = userService.update(id, user)
                .map(u -> userMapper.userToVM(u));
        return ResponseUtil.wrapOrNotFound(updatedUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(userService.get(id)
                .map(user -> userMapper.userToVM(user)));
    }

    @PermissionCheck(value = Delete.VALUE, description = Delete.DESCRIPTION)
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        userService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改密码
     */
    @PostMapping("modifyPwd")
    public ResponseEntity<Integer> changePassword(@RequestBody PasswordChangeDTO dto) {
        return new ResponseEntity<>(userService.changePassword(dto.getOldPassword(),
                dto.getNewPassword(), dto.getNewPasswordConfirm()), HttpStatus.OK);
    }

    /**
     * 重置密码
     */
    @PutMapping("resetPwd")
    public ResponseEntity resetPassword(@RequestParam Long[] ids) {
        userService.resetPwd(ids);
        return new ResponseEntity<>("重置完成", HttpStatus.OK);
    }

    /**
     * 查询用户的角色id
     *
     * @param userId 用户id
     * @return roleId
     */
    @GetMapping("/{id}/roles/id")
    public List<Long> userRoleIds(@PathVariable("id") Long userId) {
        return userService.findUserRoleIds(userId);
    }


    @GetMapping("/{id}/roles/name")
    public List<String>  userRoleName(@PathVariable("id") Long userId) {
        return userService.findUserRoleName(userId);
    }


    String[] headers = new String[]{"登录名","姓名","机构","用户类型","角色","联系电话","登录次数","最后登录时间","最后登录IP","注册时间","过期时间"};

    /**
     * 下载导出
     *
     * @param userQuery
     */
    @GetMapping("export")
    public void download(UserQuery userQuery, HttpServletRequest request, HttpServletResponse response) {
        if (Objects.isNull(userQuery.getOrganizationId())) {
            userQuery.setOrganizationId(BaseContextHandler.getOrgId());
        }
        if (Objects.isNull(userQuery.getFinanceId())) {
            userQuery.setFinanceId(BaseContextHandler.getFinanceId());
        }
        List<UserVM> data = userService.exportQuery(userQuery);
        download("用户", headers, data, UserVM::toRow, request, response);
    }

    /**
     * 下拉选
     *
     * @return
     */
    @GetMapping("/options")
    public ResponseEntity<List<SelectOption>> options() {
        return ResponseEntity.ok(userService.optionsByOrgId());
    }

    @GetMapping("/token/{loginName}")
    public ResponseEntity<String> getTokenByLoginName(@PathVariable("loginName") String loginName){
        return ResponseEntity.ok(userService.getAuthTokenFromRedis(loginName));
    }
}

