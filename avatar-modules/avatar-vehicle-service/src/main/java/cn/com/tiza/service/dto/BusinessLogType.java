package cn.com.tiza.service.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * 业务日志类型
 *
 * @author tz0920
 */

@EnumMapping("value")
public enum BusinessLogType implements BaseEnum {
    /**
     * 车辆删除
     */
    VEHICLE_DELETE("车辆删除", 1),
    /**
     * 移机
     */
    MOVE("移机", 2),
    /**
     * 销户
     */
    CLOSE_ACCOUNT("销户", 3),
    /**
     * 批量延长服务期
     */
    EXTEND_SERVICE("批量延长服务期", 4),
    /**
     * 更换终端
     */
    CHANGE_TERMINAL("更换终端", 5),
    /**
     * 暂停
     */
    SUSPEND("暂停", 6),
    /**
     * 暂停恢复
     */
    SUSPEND_RESTORE("暂停恢复", 7);

    BusinessLogType(String name, int value) {
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

    public static BusinessLogType fromValue(int value) {
        for (BusinessLogType status : BusinessLogType.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
