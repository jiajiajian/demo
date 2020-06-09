package cn.com.tiza.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * @author
 */
@EnumMapping("value")
public enum AlarmType implements BaseEnum {
    /**
     * 报警
     */
    ALARM("ALARM", 1),
    /**
     * 故障
     */
    FAULT("FAULT", 2),
    /**
     * 围栏
     */
    FENCE("FENCE", 3);

    AlarmType(String name, int value) {
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

    public void setValue(int value) {
        this.value = value;
    }

    public static AlarmType fromValue(int value) {
        for (AlarmType type : AlarmType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
