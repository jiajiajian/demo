package cn.com.tiza.constant;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * 用户状态
 * @author tiza
 */
@EnumMapping("value")
public enum UserStatus implements BaseEnum {

    /**
     * 有效
     */
    ENABLED("ENABLED", 0),
    /**
     * 禁用
     */
    DISABLED("DISABLED", 1)
    ;

    UserStatus(String name, int value) {
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

    public static UserStatus fromValue(int value) {
        for(UserStatus type : UserStatus.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
    }
}
