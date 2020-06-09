package cn.com.tiza.service;

import cn.com.tiza.annotation.AlarmConsumeStrategyAnnotation;
import cn.com.tiza.dao.*;
import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.domain.AlarmInfo;
import cn.com.tiza.domain.Tla;
import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.service.dto.AlarmState;
import cn.com.tiza.service.dto.FaultEndDto;
import cn.com.tiza.service.dto.FaultStartDto;
import cn.com.tiza.service.dto.KafkaAlarmDto;
import cn.com.tiza.service.jobs.VehicleClient;
import cn.com.tiza.service.mapper.AlarmInfoMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 故障消息消费实现类
 *
 * @author tz0920
 */
@Slf4j
@Component
@AlarmConsumeStrategyAnnotation(alarmType = AlarmType.FAULT)
public class FaultConsumeServiceImpl extends BaseAlarmConsumeService implements AlarmService {
    private final String KEY = "FAULT_";
    private final String REAL = "REAL_";
    private final String BEGIN_TIME = "BEGIN_TIME";
    private final String SPN_FMI = "SPN_FMI";
    private final String TLA = "TLA";
    private final String VIN = "VIN";
    private final String NUM = "NUM";

    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private AlarmInfoMapper alarmInfoMapper;
    @Autowired
    private AlarmInfoDao alarmInfoDao;
    @Autowired
    private AlarmHistoryDao alarmHistoryDao;
    @Autowired
    private NoticeStrategyService noticeService;
    @Autowired
    private VehicleRealtimeDao vehicleRealtimeDao;
    @Autowired
    private TlaDao tlaDao;

    @Autowired
    private VehicleClient vehicleClient;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public void consume(KafkaAlarmDto kafkaAlarmDto) {
        if (kafkaAlarmDto.getState().equals(AlarmState.START)) {
            faultStart(kafkaAlarmDto.asFaultStart());
        } else {
            faultEnd(kafkaAlarmDto.asFaultEnd());
        }
    }

    public void faultStart(FaultStartDto command) {
        Vehicle vehicle = vehicleDao.findByVin(command.getVin());
        if (Objects.isNull(vehicle)) {
            log.warn("alarm start command: {}", command);
            log.warn("can not found vehicle {}", command.getVin());
            return;
        }
        command.setOrganizationId(vehicle.getOrganizationId());
        Long rootOrgId = vehicleClient.rootOrgByVin(command.getVin());
        Tla tla = tlaDao.createLambdaQuery().andEq(Tla::getOrganizationId, rootOrgId)
                .andEq(Tla::getTlaId, command.getTlaId())
                .single();
        if (Objects.nonNull(tla)) {
            command.setTla(tla.getTla());
        }
        AlarmInfo alarmInfo = alarmInfoMapper.faultDtoToEntity(command);
        alarmInfoDao.insert(alarmInfo);
        AlarmHistory alarmHistory = alarmInfoMapper.infoToHistory(alarmInfo);
        alarmHistoryDao.insert(alarmHistory);
        //更新实时表故障状态
        vehicleRealtimeDao.updateFaultStatus(command.getVin(), AlarmState.START.getValue());
        //通知用户
        noticeService.noticeUsers(alarmHistory, vehicle, false);
        //新增至redis
        UUID uuid = UUID.randomUUID();
        redisTemplate.opsForValue().set(KEY + uuid, "", 20, TimeUnit.SECONDS);
        HashMap<String, Object> map = Maps.newHashMap();
        map.put(VIN, command.getVin());
        map.put(BEGIN_TIME, String.valueOf(command.getBeginTime()));
        map.put(SPN_FMI, command.getSpnFmi());
        map.put(TLA, command.getTla());
        map.put(NUM, String.valueOf(0));
        redisTemplate.opsForHash().putAll(REAL + KEY + uuid, map);
    }

    public void faultEnd(FaultEndDto command) {
        Vehicle vehicle = vehicleDao.findByVin(command.getVin());
        if (Objects.isNull(vehicle)) {
            log.warn("fault end command: {}", command);
            log.warn("can not found vehicle {}", command.getVin());
            return;
        }
        AlarmInfo alarmInfo = alarmInfoDao.findBySpnFmiAndVin(command.getSpnFmi(), command.getVin());
        if (Objects.nonNull(alarmInfo)) {
            //删除实时报警表且更新历史报警表
            AlarmHistory alarmHistory = updateAlarm(command.getEndTime(), alarmInfo.getId());
            //更新实时表故障状态
            long count = alarmInfoDao.createLambdaQuery().andEq(AlarmInfo::getVin, command.getVin()).andEq(AlarmInfo::getAlarmType, AlarmType.FAULT).count();
            if (count == 0) {
                vehicleRealtimeDao.updateFaultStatus(command.getVin(), AlarmState.END.getValue());
            }
            //通知
            noticeService.noticeUsers(alarmHistory, vehicle, true);

        } else {
            log.warn("fault end command: {}", command);
            log.warn("can not found Alarm vin: {}, spnFmi: {}", command.getVin(), command.getSpnFmi());
        }
    }
}
