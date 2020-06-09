package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.FenceDao;
import cn.com.tiza.dao.NoticeStrategyDao;
import cn.com.tiza.dao.UserAlarmInfoDao;
import cn.com.tiza.dao.UserDao;
import cn.com.tiza.domain.*;
import cn.com.tiza.dto.*;
import cn.com.tiza.service.dto.AlarmNoticeType;
import cn.com.tiza.service.dto.NoticeMethod;
import cn.com.tiza.service.dto.NoticeStrategyDto;
import cn.com.tiza.service.dto.NoticeStrategyQuery;
import cn.com.tiza.service.mapper.NoticeStrategyMapper;
import cn.com.tiza.web.rest.WebSocketClient;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.NoticeStrategyVM;
import com.google.common.collect.Sets;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-23
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeStrategyService {

    @Autowired
    private NoticeStrategyDao noticeStrategyDao;

    @Autowired
    private NoticeStrategyMapper noticeStrategyMapper;

    @Autowired
    private AlarmContentHelper contentHelper;

    @Autowired
    private MailSenderClient mailSenderClient;

    @Autowired
    private SendSmsClient sendSmsClient;

    @Autowired
    private FenceDao fenceDao;

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private UserDao userDao;

    private UserAlarmInfoDao userAlarmInfoDao;

    public List<NoticeStrategyVM> strategies() {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            return null;
        }
        NoticeStrategyQuery query = new NoticeStrategyQuery();
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            query.setOrgType(1);
            query.setOrganizationId(BaseContextHandler.getOrgId());
        } else if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            query.setOrgType(2);
            query.setOrganizationId(BaseContextHandler.getFinanceId());
        }
        return noticeStrategyMapper.entitiesToVMList(noticeStrategyDao.strategies(query));
    }

    public Optional<NoticeStrategy> get(Long id) {
        return Optional.ofNullable(noticeStrategyDao.single(id));
    }

    public void update(NoticeStrategyDto command) {
        //管理员不可以修改
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.toString())) {
            throw new BadRequestException(ErrorConstants.ADMIN_CAN_NOT_UPDATE_NOTICE);
        }
        if (Objects.isNull(command.getRemindWay()) || command.getRemindWay().size() == 0) {
            throw new BadRequestException(ErrorConstants.REMIND_WAY_SIZE_ZERO);
        }
        NoticeStrategy noticeStrategy = noticeStrategyDao.createLambdaQuery()
                .andEq(NoticeStrategy::getOrganizationId, BaseContextHandler.getOrgId())
                .andEq(NoticeStrategy::getCode, command.getCode())
                .single();
        if (Objects.isNull(noticeStrategy)) {
            NoticeStrategy entity = noticeStrategyMapper.dtoToEntity(command);
            noticeStrategyDao.insert(entity);
        } else {
            noticeStrategyMapper.copyProps(command, noticeStrategy);
            noticeStrategyDao.updateById(noticeStrategy);
        }
    }

    public void delete(Long id) {
        noticeStrategyDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public List<SelectOption> userOptions(Integer userType, Long organizationId) {
        return noticeStrategyDao.userOptions(userType, organizationId);
    }

    public List<SelectOption> roleOptions(Integer roleType, Long rootOrganizationId, Long financeId) {
        return noticeStrategyDao.roleOptions(roleType, rootOrganizationId, financeId);
    }


    /**
     * @param history
     * @param vehicle
     * @param isEnd
     */
    @Async
    public void noticeUsers(AlarmHistory history, Vehicle vehicle, boolean isEnd) {
        //默认系统通知
        //sysNotice(history, isEnd);
        //查找通知方式及通知人员 报警/故障需要查找该车所在组织的和所有父级通知策略，围栏只需查找自己组织通知策略
        List<NoticeStrategy> strategyList;
        AlarmType alarmType = history.getAlarmType();
        if (alarmType.equals(AlarmType.ALARM) || alarmType.equals(AlarmType.FAULT)) {
            String orgPath = noticeStrategyDao.orgPath(history.getOrganizationId());
            if (!StringUtils.hasText(orgPath)) {
                return;
            }
            String[] split = orgPath.split("/");
            strategyList = noticeStrategyDao.findStrategiesForFaultOrAlarm(split, history.getAlarmType().name());
        } else {
            strategyList = noticeStrategyDao.findStrategiesForFence(history.getOrganizationId(), history.getOrgType());
        }
        if (Objects.isNull(strategyList) || strategyList.isEmpty()) {
            return;
        }
        for (int i = 0; i < strategyList.size(); i++) {
            NoticeStrategy noticeStrategy = strategyList.get(i);
            if (!StringUtils.hasText(noticeStrategy.getRemindWay())) {
                continue;
            }
            if ((Objects.isNull(noticeStrategy.getRoleIds()) && Objects.isNull(noticeStrategy.getUserIds()))) {
                continue;
            }
            log.info("-------- remindWay : {}", noticeStrategy.getRemindWay());
            List<NoticeMethod> methods = NoticeMethod.parse(noticeStrategy.getRemindWay());
            //查找需要通知的用户
            HashSet<Long> userIdSet = Sets.newHashSet();
            if (StringUtils.hasText(noticeStrategy.getRoleIds())) {
                List<Long> roleIds = Arrays.stream(noticeStrategy.getRoleIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<Long> longs = userDao.userListByRolesAndOrg(noticeStrategy.getOrganizationId(), roleIds);
                userIdSet.addAll(longs);
            }
            if (StringUtils.hasText(noticeStrategy.getUserIds())) {
                List<Long> userIds = Arrays.stream(noticeStrategy.getUserIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<Long> longs = userDao.userListByUserIdsAndStrategyType(noticeStrategy.getOrganizationId(), history.getOrgType(), userIds);
                userIdSet.addAll(longs);
            }
            if (userIdSet.isEmpty()) {
                log.info("userIdList is empty");
                return;
            }
            List<User> userList = userDao.createLambdaQuery()
                    .andIn(User::getId, userIdSet)
                    .select();
            notice(history, methods, userList, vehicle, isEnd);
        }


    }


    /**
     * @param entity
     * @param methods
     * @param users
     * @param vehicle
     * @param isEnd
     */
    private void notice(AlarmHistory entity, List<NoticeMethod> methods, List<User> users, Vehicle vehicle, boolean isEnd) {
        boolean sms = methods.contains(NoticeMethod.SMS);
        boolean email = methods.contains(NoticeMethod.EMAIL);
        List<String> smsReceivers = new ArrayList<>();
        List<String> emailReceivers = new ArrayList<>();

        List<UserAlarmInfo> userAlarms = new ArrayList<>();

        users.forEach(user -> {

            if (sms && StringUtils.hasText(user.getPhoneNumber()) && !isEnd) {
                log.debug("----- notice : {}, {}", user.getLoginName(), user.getPhoneNumber());
                smsReceivers.add(user.getPhoneNumber());
            }
            if (email && StringUtils.hasText(user.getEmailAddress()) && !isEnd) {
                log.debug("----- notice : {}, {}", user.getLoginName(), user.getEmailAddress());
                emailReceivers.add(user.getEmailAddress());
            }

            //根据用户设置的通知配置判断是否要通知用户
            boolean noticeFlag = true;
            if (user.getAppConfig() != null) {
                try {
                    AppConfig config = JsonMapper.defaultMapper().fromJson(user.getAppConfig(), AppConfig.class);
                    noticeFlag = config.checkNotice(entity.getAlarmType());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (noticeFlag) {
                userAlarms.add(new UserAlarmInfo(user.getId(), entity.getId()));
            }
        });

        //app 通知
       /* if (!userAlarms.isEmpty()) {
            userAlarmInfoDao.insertBatch(userAlarms);
        }*/

        //获取通知时 报警内容 例：发动机报警
        String noticeType = AlarmNoticeType.fromName(entity.getAlarmType().name());
        if (sms && !smsReceivers.isEmpty()) {
            buildAndSendSms(entity, vehicle, smsReceivers);
        }
        if (email && !emailReceivers.isEmpty()) {
            buildAndSendEmail(entity, vehicle, emailReceivers, noticeType);
        }
    }

    /**
     * 构建并发送邮件
     *
     * @param entity
     * @param vehicle
     * @param emailReceivers
     * @param noticeType
     */
    private void buildAndSendEmail(AlarmHistory entity, Vehicle vehicle, List<String> emailReceivers, String noticeType) {
        String subject = buildSubject(noticeType, vehicle);
        NoticeDTO dto = NoticeDTO.builder()
                .subject(subject)
                .vin(vehicle.getVin())
                .alarmTime(entity.getBeginTime())
                .noticeContent(noticeType)
                .alarmType(entity.getAlarmType())
                .receivers(emailReceivers)
                .address(entity.getAddress())
                .tla(entity.getTla())
                .build();
        buildHelperDto(entity, dto);
        //渲染邮件内容
        contentHelper.parseEmailContent(dto);
        mailSenderClient.send(dto);
    }

    /**
     * 根据报警类型设置helper的属性
     *
     * @param entity
     * @param dto
     */
    private void buildHelperDto(AlarmHistory entity, NoticeDTO dto) {
        if (AlarmType.ALARM.equals(entity.getAlarmType())) {
            dto.setAlarmItemName(noticeStrategyDao.alarmItemName(entity.getAlarmCode()));
        }
        if (AlarmType.FAULT.equals(entity.getAlarmType())) {
            String[] split = entity.getSpnFmi().split(".");
            dto.setSpn(split[0]);
            dto.setFmi(split[1]);
        }
        if (AlarmType.FENCE.equals(entity.getAlarmType())) {
            Fence fence = fenceDao.single(entity.getFenceId());
            dto.setFenceType(fence.getAlarmType());
        }
    }

    /**
     * 构建短信信息且发送
     *
     * @param entity
     * @param vehicle
     * @param smsReceivers
     */
    private void buildAndSendSms(AlarmHistory entity, Vehicle vehicle, List<String> smsReceivers) {
        NoticeDTO dto = NoticeDTO.builder()
                .vin(vehicle.getVin())
                .alarmTime(entity.getBeginTime())
                //.noticeContent(noticeType)
                .alarmType(entity.getAlarmType())
                .receivers(smsReceivers)
                .build();
        buildHelperDto(entity, dto);
        //渲染短信内容 需符合华为云短信api格式
        contentHelper.parseSmsContent(dto);
        try {
            sendSmsClient.sendSms(dto);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }


    public void sysNotice(AlarmHistory history, boolean isEnd) {
        List<Long> userList;
        AlarmType alarmType = history.getAlarmType();
        //报警/故障 不仅需要通知该车所在组织的用户，所有父级同样通知 围栏只需通知自己组织
        if (alarmType.equals(AlarmType.ALARM) || alarmType.equals(AlarmType.FAULT)) {
            userList = userDao.sysNoticeUserIdsForFaultAndAlarm(history.getOrganizationId());
        } else {
            userList = userDao.sysNoticeUserIdsForFence(history.getVin());
        }

        AlarmMessage alarmMessage = new AlarmMessage();
        Message message = new Message();
        message.setAlarmType(history.getAlarmType());
        if (isEnd) {
            message.setAmount(-1);
        } else {
            message.setAmount(1);
        }
        alarmMessage.setMessage(message);
        alarmMessage.setUserIds(userList);
        webSocketClient.handle(alarmMessage);
    }


    /**
     * 构建邮件主题
     * @param noticeContent
     * @param vehicle
     * @return
     */
    private String buildSubject(String noticeContent, Vehicle vehicle) {
        StringBuilder builder = new StringBuilder("【TIZA-Cloud】机编号：")
                .append(vehicle.getVin())
                .append("的")
                .append(noticeContent).append("信息");
        return builder.toString();
    }
}
