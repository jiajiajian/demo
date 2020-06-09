package cn.com.tiza.service.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * 服务状态
 *
 * @author tz0920
 */

@EnumMapping("value")
public enum ServiceStatus implements BaseEnum {
    /**
     * 未开通
     */
    NOT_OPEN("未开通",0),
    /**
     * 已开通
     */
    HAS_OPENED("已开通",1),
    /**
     * 暂停
     */
    SUSPENDED("暂停",2),
    /**
     * 到期
     */
    EXPIRE("到期",3)
    ;

    ServiceStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private int value;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static ServiceStatus fromValue(int value) {
        for (ServiceStatus status : ServiceStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
