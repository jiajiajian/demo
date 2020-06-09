package cn.com.tiza.web.rest.vm;

import cn.com.tiza.dto.AlarmType;
import lombok.Data;

/**
 * @author tz0920
 */
@Data
public class DashboardAlarmCountVM {
    private AlarmType alarmType;
    private int count;
}
