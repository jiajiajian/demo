package cn.com.tiza.web.rest;

import cn.com.tiza.annotation.CurrentUser;
import cn.com.tiza.constant.Constants;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.context.access.PermissionCheck;
import cn.com.tiza.dao.OrganizationDao;
import cn.com.tiza.domain.Organization;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.OrganizationService;
import cn.com.tiza.service.dto.OrganizationCommand;
import cn.com.tiza.service.mapper.OrganizationMapper;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.OrganizationVM;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.com.tiza.constant.Permissions.Organization.*;

/**
 * 机构管理
 *
 * @author tiza
 */
@RestController
@RequestMapping("organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService service;

    @Autowired
    private OrganizationMapper mapper;

    @Autowired
    private OrganizationDao organizationDao;

    /**
     * 获取当前用户对应的机构树
     *
     * @return 返回页面需要的结构体
     */
    @GetMapping
    public List<OrganizationVM> list(@RequestParam(required = false) String name, @CurrentUser UserInfo loginUser) {
        return service.getOrgList(name, loginUser);
    }

    /**
     * 创建新机构
     *
     * @param command 页面新增command
     * @return 返回ok
     */
    @PermissionCheck(value = Create.VALUE, description = Create.DESCRIPTION)
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid OrganizationCommand command,
                                 @CurrentUser UserInfo loginUser) {
        // 检查组织id在同一个父组织下唯一
        validateParentOrg(command, loginUser);
        service.checkNameUnique(null, command.getParentOrgId(), command.getOrgName());
        Organization org = service.create(command);
        return ResponseEntity.ok(org);
    }

    /**
     * 根据id更新机构
     *
     * @param id      机构id
     * @param command 更新机构参数
     * @return 返回给前端
     */
    @PermissionCheck(value = Update.VALUE, description = Update.DESCRIPTION)
    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody @Valid OrganizationCommand command,
                                 @CurrentUser UserInfo loginUser) {
        //用户不是能是融资机构
        validateParentOrg(command, loginUser);
        if (loginUser.isOrganization() && id.equals(command.getParentOrgId())) {
            //上级机构不能是本机构
            throw new BadRequestException("error.organization.update.self_org");
        }
        Optional<Organization> update = service.update(id, command, loginUser);
        return ResponseUtil.wrapOrNotFound(update);
    }

    private void validateParentOrg(OrganizationCommand command, UserInfo loginUser) {
        //用户不是能是融资机构
        Validate.isTrue(!loginUser.isFinance());
        if (loginUser.isAdmin()) {
            if (command.getParentOrgId() == null) {
                command.setParentOrgId(Constants.ROOT_ORG_ID);
            }
        } else {
            Validate.notNull(command.getParentOrgId());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<OrganizationVM> get(@PathVariable("id") Long id) {
        return ResponseUtil.wrapOrNotFound(service.get(id));
    }

    /**
     * 删除方法
     *
     * @param ids 要删除的id
     * @return 页面
     */
    @PermissionCheck(value = Delete.VALUE, description = Delete.DESCRIPTION)
    @DeleteMapping
    public ResponseEntity delete(@RequestParam("ids") Long[] ids) {
        service.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 根级组织列表
     *
     * @return
     */
    @GetMapping("/options/root")
    public List<SelectOption> rootOrgOptions() {
        return mapper.toOptionList(service.rootList());
    }

    /**
     * 判断当前用户是给定组织的父组织
     *
     * @param orgId 组织Id
     * @return currentUser.Org.path.contains(orgId)
     */
    @GetMapping("/{orgId}/check")
    public Boolean currentUserOrgIsParent(@PathVariable Long orgId) {
        if (orgId.equals(BaseContextHandler.getOrgId())) {
            return true;
        }
        Organization org = organizationDao.single(orgId);
        return org.isParent(BaseContextHandler.getOrgId());
    }

    /**
     * 所有下级id
     *
     * @param orgId 当前组织id
     * @return 下级ids
     */
    @GetMapping("{orgId}/children")
    public List<Long> childrenIds(@PathVariable Long orgId) {
        return organizationDao.getChild(orgId)
                .stream()
                .map(Organization::getId)
                .filter(id -> !orgId.equals(id))
                .collect(Collectors.toList());
    }

    /**
     * 根据用户类型加查询跟组织下拉选 管理员查询所有跟组织 其他用户查询自己根组织
     *
     * @return
     */
    @GetMapping("/rootOrgOptionsByUserType")
    public List<SelectOption> rootOrgOptionsByUserType() {
        return mapper.toOptionList(service.rootOrgOptionsByUserType());
    }

    /**
     * 获取所有子组织
     *
     * @return 自组织列表
     */
    @GetMapping("/children")
    public List<SelectOption> getChildrenOrgs() {
        return mapper.toOptionList(service.getChildrenOrgsByUserType());
    }

}
