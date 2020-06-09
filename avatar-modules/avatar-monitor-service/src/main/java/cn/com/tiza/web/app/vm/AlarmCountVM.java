package cn.com.tiza.web.app.vm;

import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.service.dto.AlarmState;
import lombok.Data;

/**
 * @author tz0920
 */
@Data
public class AlarmCountVM {
    private int alarmState;
    private int count;
}
