package cn.com.tiza.web.app;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dto.Query;
import cn.com.tiza.service.AlarmHistoryService;
import cn.com.tiza.service.AlarmInfoService;
import cn.com.tiza.service.dto.AlarmHistoryQuery;
import cn.com.tiza.service.dto.AlarmInfoQuery;
import cn.com.tiza.service.dto.UserAlarmInfoQuery;
import cn.com.tiza.service.jobs.VehicleClient;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.AlarmHistoryVM;
import cn.com.tiza.web.rest.vm.AlarmInfoVM;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Controller
 * gen by beetlsql 2020-03-23
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/app/alarms")
public class AppAlarmController {

    @Autowired
    private AlarmInfoService alarmInfoService;

    @Autowired
    private AlarmHistoryService alarmHistoryService;

    /**
     * 用户未读消息数
     * @return not read num
     */
    @GetMapping("/user/count")
    public ResponseEntity<Long> userAlarmInfoCount() {
        return ResponseEntity.ok(alarmInfoService.countUserNotReadMsg(BaseContextHandler.getUserID()));
    }

    /**
     * 设置用户消息已读
     * @return update num
     */
    @PutMapping("/user")
    public ResponseEntity<Integer> updateUserAlarmRead() {
        return ResponseEntity.ok(alarmInfoService.updateMsgFlag(BaseContextHandler.getUserID()));
    }

    /**
     * 清空用户消息已读
     * @return int
     */
    @DeleteMapping("/user")
    public ResponseEntity<Integer> clearUserAlarm() {
        return ResponseEntity.ok(alarmInfoService.deleteUserAlarmMsg(BaseContextHandler.getUserID()));
    }

    /**
     * 查询用户消息，分页
     * @return alarm info
     */
    @GetMapping("/user")
    public ResponseEntity<List<AlarmHistoryVM>> userAlarmInfo(UserAlarmInfoQuery query) {
        query.setUserId(BaseContextHandler.getUserID());
        PageQuery<AlarmHistoryVM> pageQuery = query.toPageQuery();
        return ResponseEntity.ok(alarmInfoService.pageQueryUserMsg(pageQuery).getList());
    }
}


