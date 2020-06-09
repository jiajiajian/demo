package cn.com.tiza.service;

import cn.com.tiza.annotation.AlarmConsumeStrategyAnnotation;
import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.dao.AlarmInfoDao;
import cn.com.tiza.dao.FenceDao;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.domain.AlarmInfo;
import cn.com.tiza.domain.Fence;
import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.service.dto.AlarmState;
import cn.com.tiza.service.dto.FenceEndDto;
import cn.com.tiza.service.dto.FenceStartDto;
import cn.com.tiza.service.dto.KafkaAlarmDto;
import cn.com.tiza.service.mapper.AlarmInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 围栏报警消息消费实现类
 *
 * @author tz0920
 */
@Slf4j
@Component
@AlarmConsumeStrategyAnnotation(alarmType = AlarmType.FENCE)
public class FenceConsumeServiceImpl extends BaseAlarmConsumeService implements AlarmService {
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private AlarmInfoMapper alarmInfoMapper;
    @Autowired
    private AlarmInfoDao alarmInfoDao;
    @Autowired
    private AlarmHistoryDao alarmHistoryDao;
    @Autowired
    private FenceDao fenceDao;
    @Autowired
    private NoticeStrategyService noticeService;

    @Override
    public void consume(KafkaAlarmDto kafkaAlarmDto) {
        if (kafkaAlarmDto.getState().equals(AlarmState.START)) {
            fenceStart(kafkaAlarmDto.asFenceStart());
        } else {
            fenceEnd(kafkaAlarmDto.asFenceEnd());
        }
    }

    public void fenceStart(FenceStartDto command) {
        Vehicle vehicle = vehicleDao.findByVin(command.getVin());
        if (Objects.isNull(vehicle)) {
            log.warn("fence start command: {}", command);
            log.warn("can not found vehicle {}", command.getVin());
            return;
        }
        //查询对应围栏策略
        Fence fence = fenceDao.single(command.getFenceId());
        if (Objects.isNull(fence)) {
            log.warn("can not found fence {}", command.getFenceId());
            return;
        }
        command.setOrganizationId(fence.getOrganizationId());
        AlarmInfo alarmInfo = alarmInfoMapper.fenceAlarmDtoToEntity(command);
        alarmInfoDao.insert(alarmInfo);
        AlarmHistory alarmHistory = alarmInfoMapper.infoToHistory(alarmInfo);
        alarmHistoryDao.insert(alarmHistory);
        //通知用户
        noticeService.noticeUsers(alarmHistory, vehicle, false);
    }

    public void fenceEnd(FenceEndDto command) {
        Vehicle vehicle = vehicleDao.findByVin(command.getVin());
        if (Objects.isNull(vehicle)) {
            log.warn("fenceAlarm end command: {}", command);
            log.warn("can not found vehicle {}", command.getVin());
            return;
        }
        AlarmInfo alarmInfo = alarmInfoDao.findByFenceIdAndVin(command.getFenceId(), command.getVin());
        if (Objects.nonNull(alarmInfo)) {
            //删除实时报警表且更新历史报警表
            AlarmHistory alarmHistory = updateAlarm(command.getEndTime(), alarmInfo.getId());
            //通知
            noticeService.noticeUsers(alarmHistory, vehicle, true);
        } else {
            log.warn("fenceAlarm end command: {}", command);
            log.warn("can not found Alarm vin: {}, fenceId: {}", command.getVin(), command.getFenceId());
        }
    }
}
