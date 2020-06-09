package cn.com.tiza.service;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.FinanceDao;
import cn.com.tiza.dao.OrganizationDao;
import cn.com.tiza.dao.RoleUserDao;
import cn.com.tiza.dao.UserDao;
import cn.com.tiza.domain.AppConfig;
import cn.com.tiza.domain.RoleUser;
import cn.com.tiza.domain.User;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.UserCommand;
import cn.com.tiza.service.dto.UserQuery;
import cn.com.tiza.service.mapper.UserMapper;
import cn.com.tiza.util.EntityValidator;
import cn.com.tiza.web.rest.errors.*;
import cn.com.tiza.web.rest.vm.UserVM;
import com.google.common.collect.ImmutableMap;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.com.tiza.web.rest.errors.ErrorConstants.LOGIN_ALREADY_USED_TYPE;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

/**
 * @author 0837
 */
@Slf4j
@Service
public class UserService {

    private static final String ADMIN = "admin";

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleUserDao roleUserDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LockService lockService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private FinanceDao financeDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PageQuery<UserVM> findAll(UserQuery userQuery) {
        PageQuery<UserVM> pageQuery = userQuery.toPageQuery();
        pageQuery.setOrderBy("u.id desc");
        userDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public List<UserVM> exportQuery(UserQuery userQuery) {
        return userDao.exportQuery(userQuery.params());
    }

    private String getRoleNamesByUserId(Long userId) {
        return String.join(",", userDao.getRolesByUserId(userId, BaseContextHandler.getOrgId()));
    }

    private  String getOrgNameByOrgId(Long orgId){
        return Optional.ofNullable(organizationDao.single(orgId)).map(org->org.getOrgName()).orElse("");
    }

    private String getFinaNameByFinId(Long financeId){
        return  Optional.ofNullable(financeDao.single(financeId)).map(finance -> finance.getName()).orElse("");
    }


    /**
     * check user.loginName unique
     *
     * @param id        uid
     * @param loginName loginName
     */
    public void checkLoginNameUnique(Long id, String loginName) {
        Optional<User> existingUser = findByUserName(loginName);
        EntityValidator.checkUnique(existingUser, id, LOGIN_ALREADY_USED_TYPE);
    }

    /**
     * 创建新账户
     * command
     *
     * @param command 前台传过来的实体
     * @return User
     */
    public User create(UserCommand command) {
        command.validateUserType();
        User user = userMapper.commandToUser(command);
        user.setLoginPassword(passwordEncoder.encode(md5DigestAsHex(Constants.DEFAULT_PWD.getBytes())));
        user.setCreatorInfo();
        userDao.insert(user, true);
        log.debug("Created Information for User: {}", user);
        addRoleUser(user.getId(), command.getRoleIds());
        return user;
    }

    /**
     * 更新账户信息
     *
     * @param id      账户id
     * @param command 实体类
     * @return Optional<User>
     */
    public Optional<User> update(Long id, UserCommand command) {
        return get(id).map(user -> {
            //用户类型不能修改
            command.setUserType(user.getUserType());
            command.validateUserType();
            user.setLoginName(command.getLoginName());
            if (UserType.ORGANIZATION.equals(user.getUserType())) {
                user.setOrganizationId(command.getOrganizationId());
            }
            //融资机构不能修改
//            user.setFinanceId(command.getFinanceId());
            user.setRealname(command.getRealName());
            user.setPhoneNumber(command.getPhoneNumber());
            user.setTelephoneNumber(command.getTelephoneNumber());
            user.setEmailAddress(command.getEmailAddress());
            user.setExpirationDate(command.getExpirationDate());
            user.setUpdateInfo();
            userDao.updateById(user);
            roleUserDao.deleteByUserId(id);
            this.addRoleUser(id, command.getRoleIds());
            return user;
        });
    }

    private void addRoleUser(Long userId, List<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                roleUserDao.insert(new RoleUser(roleId, userId));
            }
        }
    }

    public Optional<User> getAppUserInfo(Long id) {
        Optional<User> optional = Optional.ofNullable(userDao.single(id));
        return optional.map(user -> {
            user.setRoleName(getRoleNamesByUserId(user.getId()));
            if(Objects.nonNull(user.getOrganizationId())){
                user.setOrgName(getOrgNameByOrgId(user.getOrganizationId()));
            }else if(Objects.nonNull(user.getFinanceId())){
                user.setFinanceName(getFinaNameByFinId(user.getFinanceId()));
            }
            return user;
        });
    }

    /**
     * 用户详情
     *
     * @param id user.id
     * @return user info with role ids
     */
    public Optional<User> get(Long id) {
        Optional<User> optional = Optional.ofNullable(userDao.single(id));
        return optional.map(user -> {
            user.setRoleIds(userDao.getRoleIdByUserId(id));
            return user;
        });
    }

    public Optional<User> findByUserName(String username) {
        return userDao.findByLoginName(username);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            get(id).ifPresent(user -> {
                if (ADMIN.equals(user.getLoginName())) {
                    throw new BadRequestException(ErrorConstants.ADMIN_CAN_NOT_BE_DELETED);
                }
                user.setUpdateInfo();
                user.setDelFlag(true);
                userDao.updateById(user);

                //删除所有的用户角色关联
                roleUserDao.deleteByUserId(id);
            });

        }
    }

    public void resetPwd(Long[] ids) {
        for (Long id : ids) {
            User user = userDao.unique(id);
            user.setLoginPassword(passwordEncoder.encode(md5DigestAsHex(Constants.DEFAULT_PWD.getBytes())));
            userDao.updateTemplateById(user);
        }
    }

    /**
     * 修改密码
     *
     * @param oldPassword        旧密码
     * @param newPassword        新密码
     * @param newPasswordConfirm 确认密码
     * @return 1：新密码跟确认密码不一致 2：原密码错误 3:修改成功
     */
    public Integer changePassword(String oldPassword, String newPassword, String newPasswordConfirm) {
        if (!newPassword.equals(newPasswordConfirm)) {
            return 1;
        }
        String loginName = BaseContextHandler.getLoginName();
        return findByUserName(loginName).map(user -> {
            if (passwordEncoder.matches(oldPassword, user.getLoginPassword())) {
                user.setLoginPassword(passwordEncoder.encode(newPassword));
                userDao.updateTemplateById(user);
                return 3;
            } else {
                return 2;
            }
        }).orElse(2);
    }

    public Optional<UserVM> login(String username, String password) {
        if (lockService.isUserLocked(username)) {
            throw new UserLockedException(lockService.getExpire(username).toString());
        }
        Optional<User> byUserName = findByUserName(username);
        byUserName.orElseThrow(()-> new BadRequestAlertException("username not exist", null, SystemErrorConstants.USERNAME_NOT_MATCH));
        return byUserName.map(user -> {
            if (passwordEncoder.matches(password, user.getLoginPassword())) {
                user.checkEnable();
                lockService.clear(username);
                if (user.isOrganization()) {
                    user.setRootOrgId(organizationDao.unique(user.getOrganizationId()).getRootOrgId());
                }
                //添加用户登陆次数和最后登录IP
                User newUser =  new User();
                newUser.setLatestLoginIp((String)BaseContextHandler.get(Constants.CONTEXT_KEY_USER_IP));
                if(Objects.isNull(user.getLoginTimes())){
                    newUser.setLoginTimes(1);
                }else{
                    newUser.setLoginTimes(user.getLoginTimes()+1);
                }
                newUser.setLatestLoginTime(System.currentTimeMillis());
                newUser.setId(user.getId());
                userDao.updateTemplateById(newUser);
                return userMapper.userToVM(user);
            } else {
                if (lockService.increaseUserAttempt(username)) {
                    throw new UserLockedException(lockService.getExpire(username).toString());
                } else {
                    Map<String, Object> tip = ImmutableMap.of("errorNum", lockService.getUserAttempt(username),
                            "retryNum", lockService.getRetryNum(username));
                    throw new UsernamePasswordNotMatchException(tip);
                }
            }
        });
    }

    /**
     * 查询用户的角色id
     *
     * @param userId 用户id
     * @return roleId
     */
    public List<Long> findUserRoleIds(Long userId) {
        return roleUserDao.selectByUserId(userId)
                .stream()
                .map(RoleUser::getRoleId)
                .collect(Collectors.toList());
    }

   public List<String> findUserRoleName(Long userId){
        return roleUserDao.findUserRoleName(userId);
   }

    public void updateAppConfig(Long userId, AppConfig command) {
        Optional.ofNullable(userDao.single(userId)).ifPresent(user -> {
            user.setAppConfig(JsonMapper.defaultMapper().toJson(command));
            userDao.updateById(user);
        });
    }

    public List<SelectOption> optionsByOrgId() {
        return userDao.optionsByOrgId(BaseContextHandler.getOrgId());
    }

    public void clear(String username) {
        redisTemplate.opsForHash().delete("USER_TOKEN", username);
    }

    public String getAuthTokenFromRedis(String loginName){
         return Optional.ofNullable(redisTemplate.opsForHash().get("USER_TOKEN", loginName))
                 .map(item->item.toString())
                 .orElseGet(String::new);
    }
}
