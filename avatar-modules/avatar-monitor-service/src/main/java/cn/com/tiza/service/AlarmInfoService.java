package cn.com.tiza.service;

import cn.com.tiza.dao.AlarmInfoDao;
import cn.com.tiza.dao.UserAlarmInfoDao;
import cn.com.tiza.service.dto.AlarmHistoryQuery;
import cn.com.tiza.service.dto.AlarmInfoQuery;
import cn.com.tiza.web.rest.vm.AlarmHistoryVM;
import cn.com.tiza.web.rest.vm.AlarmInfoVM;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmInfoService {

    @Autowired
    private AlarmInfoDao alarmInfoDao;

    @Autowired
    private UserAlarmInfoDao userAlarmInfoDao;

    public PageQuery<AlarmInfoVM> pageQueryCurrentVehicleFault(AlarmInfoQuery query) {
        PageQuery<AlarmInfoVM> pageQuery = query.toPageQuery();
        alarmInfoDao.pageQueryCurrentVehicleFault(pageQuery);
        return pageQuery;
    }

    public Long countUserNotReadMsg(Long userId) {
        return userAlarmInfoDao.countByUserNotRead(userId);
    }

    public Integer updateMsgFlag(Long userId) {
        return userAlarmInfoDao.updateMsgRead(userId);
    }

    public Integer deleteUserAlarmMsg(Long userId) {
        return userAlarmInfoDao.deleteByUser(userId);
    }

    public PageQuery<AlarmHistoryVM> pageQueryUserMsg(PageQuery<AlarmHistoryVM> pageQuery) {
        userAlarmInfoDao.pageQueryMsg(pageQuery);
        return pageQuery;
    }

}
