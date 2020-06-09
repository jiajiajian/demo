package cn.com.tiza.service;

import cn.com.tiza.annotation.AlarmConsumeStrategyAnnotation;
import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.dao.AlarmInfoDao;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.domain.AlarmInfo;
import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.service.dto.AlarmEndDto;
import cn.com.tiza.service.dto.AlarmStartDto;
import cn.com.tiza.service.dto.AlarmState;
import cn.com.tiza.service.dto.KafkaAlarmDto;
import cn.com.tiza.service.mapper.AlarmInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 报警项消息消费实现类
 *
 * @author tz0920
 */
@Slf4j
@Component
@AlarmConsumeStrategyAnnotation(alarmType = AlarmType.ALARM)
public class AlarmConsumeServiceImpl extends BaseAlarmConsumeService implements AlarmService {
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

    @Override
    public void consume(KafkaAlarmDto kafkaAlarmDto) {
        if (kafkaAlarmDto.getState().equals(AlarmState.START)) {
            alarmStart(kafkaAlarmDto.asAlarmStart());
        } else {
            alarmEnd(kafkaAlarmDto.asAlarmEnd());
        }
    }

    public void alarmStart(AlarmStartDto command) {
        Vehicle vehicle = vehicleDao.findByVin(command.getVin());
        if (Objects.isNull(vehicle)) {
            log.warn("alarm start command: {}", command);
            log.warn("can not found vehicle {}", command.getVin());
            return;
        }
        command.setOrganizationId(vehicle.getOrganizationId());
        AlarmInfo alarmInfo = alarmInfoMapper.alarmDtoToEntity(command);
        alarmInfoDao.insert(alarmInfo);
        AlarmHistory alarmHistory = alarmInfoMapper.infoToHistory(alarmInfo);
        alarmHistoryDao.insert(alarmHistory);
        //通知用户
        noticeService.noticeUsers(alarmHistory, vehicle, false);
    }

    public void alarmEnd(AlarmEndDto command) {
        Vehicle vehicle = vehicleDao.findByVin(command.getVin());
        if (Objects.isNull(vehicle)) {
            log.warn("alarm end command: {}", command);
            log.warn("can not found vehicle {}", command.getVin());
            return;
        }
        AlarmInfo alarmInfo = alarmInfoDao.findByAlarmCodeAndVin(command.getVin(), command.getAlarmItem());
        if (Objects.nonNull(alarmInfo)) {
            //删除实时报警表且更新历史报警表
            AlarmHistory alarmHistory = updateAlarm(command.getEndTime(), alarmInfo.getId());
            //通知用户
            noticeService.noticeUsers(alarmHistory, vehicle, true);
        } else {
            log.warn("alarm end command: {}", command);
            log.warn("can not found Alarm vin: {}, alarmItem: {}, beginTime: {}", command.getVin(), command.getAlarmItem());
        }
    }
}
