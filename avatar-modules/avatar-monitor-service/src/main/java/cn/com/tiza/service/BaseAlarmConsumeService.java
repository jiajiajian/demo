package cn.com.tiza.service;

import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.dao.AlarmInfoDao;
import cn.com.tiza.domain.AlarmHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报警项消息消费实现类
 *
 * @author tz0920
 */
@Slf4j
@Component
public class BaseAlarmConsumeService {
    @Autowired
    private AlarmInfoDao alarmInfoDao;
    @Autowired
    private AlarmHistoryDao alarmHistoryDao;


    /**
     * 删除实时报警表且更新历史报警表
     *
     * @param endTime
     * @param alarmInfoId 报警id
     */
    public AlarmHistory updateAlarm(Long endTime, Long alarmInfoId) {
        //从实时报警中删除
        alarmInfoDao.deleteById(alarmInfoId);
        //更新历史报警信息
        AlarmHistory alarmHistory = alarmHistoryDao.single(alarmInfoId);
        alarmHistory.setEndTime(endTime);
        alarmHistory.setAlarmState(1);
        alarmHistoryDao.updateById(alarmHistory);
        return alarmHistory;
    }
}
