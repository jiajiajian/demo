package cn.com.tiza.domain;

import cn.com.tiza.dto.AlarmType;
import lombok.Data;

/**
 * @author tiza
 */
@Data
public class AppConfig {

    /**
     * app消息通知
     */
    private Integer msgFlag;

    /**
     * 系统语言
     */
    private String sysLang;

    public boolean checkNoMsg() {
        return msgFlag != null && 0 == msgFlag;
    }

    public boolean checkNotice(AlarmType alarmType) {
        if(checkNoMsg()) {
            return false;
        } else {
            switch (alarmType) {
                case FAULT:
                case ALARM:
                case FENCE:
                    return true;
                default:
                    return false;
            }
        }
    }
}
