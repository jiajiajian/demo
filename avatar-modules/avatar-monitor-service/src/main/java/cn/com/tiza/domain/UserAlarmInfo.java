package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

/**
 * 用户消息
 * @author tiza
 */
@Data
@Table(name = "v_user_alarm_info")
public class UserAlarmInfo {

    @AutoID
    private Long id;

    private Long userId;

    private Long alarmId;

    private Boolean readFlag;

    /**
     * 创建时间
     */
    private Long createTime;

    public UserAlarmInfo() {
    }

    public UserAlarmInfo(Long userId, Long alarmId) {
        this.userId = userId;
        this.alarmId = alarmId;
        this.readFlag = false;
    }
}
