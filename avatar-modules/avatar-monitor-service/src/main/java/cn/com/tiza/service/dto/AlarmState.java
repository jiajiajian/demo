package cn.com.tiza.service.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * @author
 */
@EnumMapping("value")
public enum AlarmState implements BaseEnum {
    /**
     * 报警
     */
    START("START", 0),
    /**
     * 故障
     */
    END("END", 1);


    AlarmState(String name, int value) {
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

    public static AlarmState fromValue(int value) {
        for (AlarmState type : AlarmState.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
