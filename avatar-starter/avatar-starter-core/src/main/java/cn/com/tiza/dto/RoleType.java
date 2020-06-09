package cn.com.tiza.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * 角色类型
 * @author tiza
 */
@EnumMapping("value")
public enum RoleType implements BaseEnum {

    /**
     * 租户管理员
     */
    ORGANIZATION("机构", 2),
    /**
     * 普通用户
     */
    FINANCE("融资机构", 3);

    RoleType(String name, int value) {
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

    public static RoleType fromValue(int value) {
        for (RoleType type : RoleType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
