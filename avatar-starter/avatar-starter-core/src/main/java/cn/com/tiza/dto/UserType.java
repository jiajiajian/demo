package cn.com.tiza.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * 用户类型
 * @author tiza
 */
@EnumMapping("value")
public enum UserType implements BaseEnum {

    /**
     * 系统管理员
     */
    ADMIN("管理员", 1),
    /**
     * 租户管理员
     */
    ORGANIZATION("机构用户", 2),
    /**
     * 普通用户
     */
    FINANCE("融资用户", 3);

    UserType(String name, int value) {
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

    public static UserType fromValue(int value) {
        for (UserType type : UserType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
