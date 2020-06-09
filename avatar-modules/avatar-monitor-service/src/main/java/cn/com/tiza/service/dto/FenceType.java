package cn.com.tiza.service.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * @author
 */
@EnumMapping("value")
public enum FenceType implements BaseEnum {
    /**
     * 图形围栏
     */
    CHART("图形围栏", 1),
    /**
     * 行政围栏
     */
    ADMINISTRATION("行政围栏", 2),
    /**
     * 时间围栏
     */
    TIME("时间围栏", 3);


    FenceType(String name, int value) {
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

    public static FenceType fromValue(int value) {
        for (FenceType type : FenceType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
