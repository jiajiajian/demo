package cn.com.tiza.web.rest;

import cn.com.tiza.context.UserInfo;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.web.rest.dto.LogCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 用户相关api接口
 *
 * @author tiza
 */
//@FeignClient(value = "feisi-SYSTEM", fallback = HystrixAccountApiFallback.class)
@FeignClient(value = "feisi-system")
public interface AccountApiClient {

    /**
     * 从token解析用户信息
     * @param token token
     * @return login user info
     */
    @GetMapping("/auth/parse")
    UserInfo parseToken(@RequestParam("token") String token);

    /**
     * 判断当前用户是给定组织的父组织
     * @param orgId 组织Id
     * @return currentUser.Org.path.contains(orgId)
     */
    @GetMapping("/organizations/{id}/check")
    Boolean currentUserOrgIsParent(@PathVariable("id") Long orgId);

    /**
     * 获取用户是否有权限
     *
     * @param permissions 菜单Id
     * @return boolean 是否有权限
     */
    @GetMapping(value = "/accounts/permission/check", consumes = APPLICATION_JSON_VALUE)
    boolean checkPermission(@RequestParam("permission") int[] permissions);

    /**
     * 保存系统操作日志
     *
     * @param log 日志内容
     * @return
     */
    @PostMapping(value = "/logs", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity create(@RequestBody @Valid LogCommand log);


    /**
     * 查询用户的角色id
     * @param userId 用户id
     * @return roleId
     */
    @GetMapping("/users/{id}/roles/id")
    List<String> findUserRoleIds(@PathVariable("id") Long userId);

    /**
     * 所有下级id
     * @param orgId 当前组织id
     * @return 下级ids
     */
    @GetMapping("/organizations/{orgId}/children")
    List<Long> findChildOrgIds(@PathVariable("orgId") Long orgId);

    /**
     * 所有下级组织
     * @return 下级组织
     */
    @GetMapping("/organizations/children")
    List<SelectOption> getChildrenOrgs();

    /**
     * 查询用户角色名称
     * @param userId
     * @return
     */
    @GetMapping("/users/{id}/roles/name")
    List<String> findUserRoleName(@PathVariable("id") Long userId);

    /**
     * 查询登陆用户redis中是否缓存token信息
     * @param loginName
     * @return
     */
    @GetMapping("/users/token/{loginName}")
    String getTokenByLoginName(@PathVariable("loginName") String loginName);

}
